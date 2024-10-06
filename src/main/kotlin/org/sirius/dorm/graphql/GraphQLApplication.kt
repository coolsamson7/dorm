package org.sirius.dorm.graphql

import graphql.ExecutionResult
import graphql.GraphQL
import graphql.Scalars.*
import graphql.execution.*
import graphql.schema.*
import graphql.schema.GraphQLArgument.newArgument
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLInputObjectField.newInputObjectField
import graphql.schema.GraphQLInputObjectType.newInputObject
import graphql.schema.GraphQLObjectType.newObject
import graphql.schema.idl.SchemaPrinter
import jakarta.annotation.PostConstruct
import lombok.extern.slf4j.Slf4j
import org.sirius.common.type.base.int
import org.sirius.common.type.base.string
import org.sirius.dorm.DORMConfiguration
import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.Multiplicity
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.attribute
import org.sirius.dorm.model.relation
import org.sirius.dorm.`object`.DataObject
import org.sirius.dorm.query.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture


class TransactionalExecutionStrategy(val objectManager: ObjectManager, exceptionHandler: DataFetcherExceptionHandler) : AsyncExecutionStrategy(exceptionHandler) {
    // override

    override fun execute(executionContext: ExecutionContext, parameters: ExecutionStrategyParameters): CompletableFuture<ExecutionResult> {
        objectManager.begin()

        return super.execute(executionContext, parameters)
            .whenComplete { _, throwable ->
                if (throwable != null)
                   objectManager.rollback()
                else
                   objectManager.commit()
            }
    }
}

class QueryBuilder(val objectManager: ObjectManager) {
    val queryManager = objectManager.queryManager()

    // public

    fun buildQuery(descriptor: ObjectDescriptor, filter: Any?) : Query<DataObject> {
        val query = queryManager.create()
        val type = queryManager.from(descriptor)

        query
            .select(type)
            .from(type)

        if ( filter !== null)
            query.where(buildExpression(type, filter as Map<String,Any>))

        return query
    }

    // private

    private fun buildExpression(root: FromRoot, filter: Map<String,Any>) : ObjectExpression {
        if ( filter.containsKey("and")) {
            val expressions = filter.get("and") as List<Any>

            return and(*expressions.map { child -> buildExpression(root, child as Map<String,Any>) as BooleanExpression}.toTypedArray())
        }

        else if ( filter.containsKey("or")) {
            val expressions = filter.get("or") as List<Any>

            return or(*expressions.map { child -> buildExpression(root, child as Map<String,Any>) as BooleanExpression}.toTypedArray())
        }

        else {
            // must be one of the different

            val key = filter.keys.iterator().next()

            val property = root.objectDescriptor.property(key)

            when ( property.asAttribute().baseType()) {
                Short::class.java -> return buildNumericPredicate(root.get(key), filter.get(key) as Map<String,Any> )
                Int::class.java -> return buildNumericPredicate(root.get(key), filter.get(key) as Map<String,Any>)
                Integer::class.java -> return buildNumericPredicate(root.get(key), filter.get(key) as Map<String,Any>)
                Long::class.java -> return buildNumericPredicate(root.get(key), filter.get(key) as Map<String,Any>)
                Float::class.java -> return buildNumericPredicate(root.get(key), filter.get(key) as Map<String,Any>)
                Double::class.java -> return buildNumericPredicate(root.get(key), filter.get(key) as Map<String,Any>)
                String::class.java -> return buildStringPredicate(root.get(key), filter.get(key) as Map<String,Any>)
                else -> {
                    throw Error("unsupported type ${property.asAttribute().baseType()}")
                }
            }
        }
    }

    private fun buildNumericPredicate(path: ObjectPath, filter: Map<String,Any>): ObjectExpression {
        return when (val predicate = filter.keys.iterator().next()) {
            "lt" -> lt(path, filter.get(predicate)!!)
            "le" -> le(path, filter.get(predicate)!!)
            "gt" -> gt(path, filter.get(predicate)!!)
            "ge" -> ge(path, filter.get(predicate)!!)
            "eq" -> eq(path, filter.get(predicate)!!)
            "ne" -> ne(path, filter.get(predicate)!!)
            else -> {
                throw Error("ouch")
            }
        }
    }
    private fun buildStringPredicate(path: ObjectPath, filter: Map<String,Any>): ObjectExpression {
        return when (val predicate = filter.keys.iterator().next()) {
            "eq" -> eq(path, filter.get(predicate)!!)
            "ne" -> ne(path, filter.get(predicate)!!)
            else -> {
             throw Error("ouch")
            }
        }
    }
}

class SchemaBuilder(val objectManager: ObjectManager) {
    private val queryBuilder = QueryBuilder(objectManager)

    // public

    fun createSchema() : GraphQLSchema? {
        val types = HashSet<GraphQLType>()

        // common input

        val stringFilter = stringFilter()
        val intFilter = intFilter()

        // iterate over object definitions

        for ( descriptor in objectManager.descriptors()) {
            val newObject = newObject().name(descriptor.name)

            for ( field in descriptor.properties) {
                if ( field.isAttribute())
                    newObject.field(
                        newFieldDefinition()
                            .name(field.name)
                            .dataFetcher {
                                it.getSource<DataObject>()!!.get(it.field.name)
                            }
                            .type(type4(field.asAttribute().baseType()))
                    )
                else {
                    if ( field.asRelation().multiplicity.mutliValued) {
                        newObject.field(
                            newFieldDefinition()
                                .name(field.name)
                                .dataFetcher {
                                    it.getSource<DataObject>()!!.get(it.field.name)
                                }
                                .type(GraphQLList.list((GraphQLTypeReference.typeRef(field.asRelation().target))))
                        )
                    }
                    else {
                        newObject.field(
                            newFieldDefinition()
                                .name(field.name)
                                .dataFetcher {
                                    it.getSource<DataObject>()!!.get(it.field.name)
                                }
                                .type(GraphQLTypeReference.typeRef(field.asRelation().target))
                        )
                    }
                }
            } // for

            types.add(newObject.build())
        }

        // query

        val query = newObject().name("Query")

        for ( descriptor in objectManager.descriptors()) {
            // filter

            val filterBuilder = newInputObject()
                .name("${descriptor.name}Filter")
                .field(
                    newInputObjectField()
                        .name("and")
                        .type(GraphQLList.list(GraphQLTypeReference.typeRef("${descriptor.name}Filter")))
                )
                .field(
                    newInputObjectField()
                        .name("or")
                        .type(GraphQLList.list(GraphQLTypeReference.typeRef("${descriptor.name}Filter"))
                        ))

            // check all properties

            for ( property in descriptor.properties) {
                if ( property.isAttribute()) {
                    filterBuilder.field(
                        newInputObjectField()
                            .name(property.name)
                            .type(when ( property.asAttribute().baseType()) {
                                String::class.javaObjectType -> stringFilter
                                Short::class.javaObjectType -> intFilter
                                Int::class.javaObjectType -> intFilter
                                Long::class.javaObjectType -> intFilter
                                else -> {
                                    throw Error("unsupported type ${property.asAttribute().baseType()}")
                                }
                            })
                    )
                }
            }

            // done

            val filter = filterBuilder.build()

            // ...

            query.field(
                newFieldDefinition()
                    .name(descriptor.name)
                    .argument(newArgument().name("filter").type(filter).build())
                    .dataFetcher {
                        executeQuery(descriptor, it)
                    }
                    .type(GraphQLList.list(GraphQLTypeReference.typeRef(descriptor.name)))
            )
        }

        val schema = GraphQLSchema.newSchema()
            .query(query.build())
            .additionalTypes(types)
            .build()

        println(SchemaPrinter().print(schema))


        return schema
    }

    // private

    private fun executeQuery(descriptor: ObjectDescriptor, environment: DataFetchingEnvironment) : List<DataObject> {
        return queryBuilder.buildQuery(descriptor, environment.getArgument<Any>("filter")).execute().getResultList()
    }

    private fun stringFilter() : GraphQLInputObjectType {
        return newInputObject()
            .name("StringFilter")
            .field(
                newInputObjectField()
                    .name("eq")
                    .type(GraphQLString)
            )
            .field(
                newInputObjectField()
                    .name("ne")
                    .type(GraphQLString)
            )
            .build()
    }

    private fun intFilter() : GraphQLInputObjectType {
        return newInputObject()
            .name("IntFilter")
            .field(
                newInputObjectField()
                    .name("lt")
                    .type(GraphQLInt)
            )
            .field(
                newInputObjectField()
                    .name("gt")
                    .type(GraphQLInt)
            )
            .build()
    }

    private fun type4(clazz : Class<*>) : GraphQLOutputType {
        return when ( clazz ) {
            Boolean::class.javaObjectType  -> GraphQLBoolean
            Int::class.javaObjectType  -> GraphQLInt
            Short::class.javaObjectType  -> GraphQLInt
            Long::class.javaObjectType -> GraphQLInt
            Float::class.javaObjectType -> GraphQLFloat
            Double::class.javaObjectType -> GraphQLFloat
            String::class.javaObjectType  -> GraphQLString
            else -> {
                throw Error("unsupported type ${clazz}")
            }
        }
    }

}

@Component
class GraphQLProvider {
    // instance data

    @Autowired
    lateinit var objectManager : ObjectManager

    lateinit var graphQL : GraphQL

    lateinit var queryBuilder : QueryBuilder

    protected fun <T> withTransaction(doIt: () -> T) : T {
        objectManager.begin()
        var committed = false

        try {
            val result = doIt()

            committed = true
            objectManager.commit()

            return result
        }
        catch (throwable: Throwable) {
            if ( !committed )
                objectManager.rollback()

            throw throwable
        }
    }

    fun setupData() {
        withTransaction {
            // create type

            objectManager.type("Person")
                .add(attribute("firstName").type(string()))
                .add(attribute("name").type(string()))
                .add(attribute("age").type(int()))

                // relations

                .add(relation("father").target("Person").multiplicity(Multiplicity.ZERO_OR_ONE).inverse("children"))
                .add(relation("children").target("Person").multiplicity(Multiplicity.ZERO_OR_MANY).inverse("father").owner())

                // done

                .register()

            // create data

            val personDescriptor = objectManager.getDescriptor("Person")

            val andi = objectManager.create(personDescriptor)

            andi["firstName"] = "Andi"
            andi["name"] = "Ernst"
            andi["age"] = 58

            // child

            val child = objectManager.create(personDescriptor)

            child["firstName"] = "Nika"
            child["name"] = "Martinez"
            child["age"] = 14

            // link

            child["father"] = andi
        }
    }

    fun query() {
        val executionResult = graphQL.execute(
            "query sampleQuery { " +
                    "   Person (filter: " +
                    "        {or: [ " +
                    "          {age: {gt: 0}}, " +
                    "          {age: {gt: 1}}" +
                    "        ]}" +
                    "      ) {\n" +
                    //"   Person (request: {select: \"SELECT p FROM Person p WHERE p.age > 0\"}) {\n" +
                    "          id\n" +
                    "          firstName" +
                    "          name" +
                    "          father {" +
                    "             firstName" +
                    "             name" +
                    "          }" +
                    "       }" +
                    "   }" +
                    "")

        println(executionResult.getData<Any>().toString())
    }

    @Bean
    fun graphQL(): GraphQL {
        return graphQL
    }

    @PostConstruct
    fun setup() {
        setupData()

        graphQL= GraphQL.newGraphQL(withTransaction {  SchemaBuilder(objectManager).createSchema()})
            .queryExecutionStrategy(TransactionalExecutionStrategy(objectManager, SimpleDataFetcherExceptionHandler()))
            .build()

        queryBuilder = QueryBuilder(objectManager)

        // test

        query()
    }
}

@Configuration()
@Import(DORMConfiguration::class)
class GraphQLApplicationConfiguration {
}


/*@Configuration
class GraphiQlConfiguration {
    @Bean
    @Order(0)
    fun graphiQlRouterFunction(): RouterFunction<ServerResponse> {
        var builder = RouterFunctions.route()
        val graphiQlPage = ClassPathResource("graphiql/index.html")
        val graphiQLHandler = GraphiQlHandler("/graphql", "", graphiQlPage)
        builder = builder.GET("/graphiql", graphiQLHandler::handleRequest)
        return builder.build()
    }
}*/
@SpringBootApplication
@Slf4j
class GraphQLApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(GraphQLApplication::class.java, *args)
        }
    }
}
