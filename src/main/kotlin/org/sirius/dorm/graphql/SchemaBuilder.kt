package org.sirius.dorm.graphql
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import graphql.Scalars
import graphql.schema.*
import graphql.schema.idl.SchemaPrinter
import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.`object`.DataObject

class SchemaBuilder(val objectManager: ObjectManager) {
    private val queryBuilder = QueryBuilder(objectManager)
    private val mutator = ObjectMutator(objectManager)

    // public

    fun createSchema() : GraphQLSchema? {
        val types = HashSet<GraphQLType>()

        // common input

        val stringFilter = stringFilter()
        val intFilter = intFilter()
        val floatFilter = floatFilter()
        val booleanFilter = booleanFilter()

        // result type for bulk operations

        types.add(GraphQLObjectType.newObject().name("OperationResult")
            .field(GraphQLFieldDefinition.newFieldDefinition()
                .name("count")
                .dataFetcher {
                    it.getSource<Int>()
                }
                .type(Scalars.GraphQLInt)
                )
            .build())

        // iterate over object definitions

        for ( descriptor in objectManager.descriptors()) {
            // create the <descriptor> type itself

            val newObject = GraphQLObjectType.newObject().name(descriptor.name)

            for ( field in descriptor.properties) {
                if ( field.isAttribute())
                    newObject.field(
                        GraphQLFieldDefinition.newFieldDefinition()
                            .name(field.name)
                            .dataFetcher {
                                it.getSource<DataObject>()!!.get(it.field.name)
                            }
                            .type(type4<GraphQLOutputType>(field.asAttribute().baseType()))
                    )
                else {
                    if ( field.asRelation().multiplicity.mutliValued) {
                        newObject.field(
                            GraphQLFieldDefinition.newFieldDefinition()
                                .name(field.name)
                                .dataFetcher {
                                    it.getSource<DataObject>()!!.get(it.field.name)
                                }
                                .type(GraphQLList.list((GraphQLTypeReference.typeRef(field.asRelation().target))))
                        )
                    }
                    else {
                        newObject.field(
                            GraphQLFieldDefinition.newFieldDefinition()
                                .name(field.name)
                                .dataFetcher {
                                    it.getSource<DataObject>()!!.get(it.field.name)
                                }
                                .type(GraphQLTypeReference.typeRef(field.asRelation().target))
                        )
                    }
                }
            } // for property

            types.add(newObject.build())

            // input object

            val inputObject = GraphQLInputObjectType.newInputObject().name("${descriptor.name}Input")

            for ( field in descriptor.properties) {
                if ( field.isAttribute())
                    inputObject.field(
                        GraphQLInputObjectField.newInputObjectField()
                            .name(field.name)
                            .type(type4<GraphQLInputType>(field.asAttribute().baseType()))
                    )
                else {
                    if ( field.asRelation().multiplicity.mutliValued) {
                        inputObject.field(
                            GraphQLInputObjectField.newInputObjectField()
                                .name(field.name)
                                .type(GraphQLList.list((GraphQLTypeReference.typeRef("${field.asRelation().target}Input"))))
                        )
                    }
                    else {
                        inputObject.field(
                            GraphQLInputObjectField.newInputObjectField()
                                .name(field.name)
                                .type(GraphQLTypeReference.typeRef("${field.asRelation().target}Input"))
                        )
                    }
                }
            } // for property

            types.add(inputObject.build())
        } // for descriptor

        // query

        val query = GraphQLObjectType.newObject().name("Query")

        for ( descriptor in objectManager.descriptors()) {
            // filter

            val filterBuilder = GraphQLInputObjectType.newInputObject()
                .name("${descriptor.name}Filter")
                .field(
                    GraphQLInputObjectField.newInputObjectField()
                        .name("and")
                        .type(GraphQLList.list(GraphQLTypeReference.typeRef("${descriptor.name}Filter")))
                )
                .field(
                    GraphQLInputObjectField.newInputObjectField()
                        .name("or")
                        .type(
                            GraphQLList.list(GraphQLTypeReference.typeRef("${descriptor.name}Filter"))
                        ))

            // check all properties

            for ( property in descriptor.properties) {
                if ( property.isAttribute()) {
                    filterBuilder.field(
                        GraphQLInputObjectField.newInputObjectField()
                            .name(property.name)
                            .type(when ( property.asAttribute().baseType()) {
                                String::class.javaObjectType -> stringFilter
                                Boolean::class.javaObjectType -> booleanFilter
                                Short::class.javaObjectType -> intFilter
                                Int::class.javaObjectType -> intFilter
                                Long::class.javaObjectType -> intFilter
                                Double::class.javaObjectType -> floatFilter
                                Float::class.javaObjectType -> floatFilter
                                else -> {
                                    throw Error("unsupported type ${property.asAttribute().baseType()}")
                                }
                            })
                    )
                }
                else {
                    if ( !property.asRelation().multiplicity.mutliValued) {
                        filterBuilder.field(
                            GraphQLInputObjectField.newInputObjectField()
                                .name(property.name)
                                .type(GraphQLTypeReference.typeRef("${property.asRelation().target}Filter"))
                        )
                    }
                }
            }

            // done

            val filter = filterBuilder.build()

            // add filter to query

            query.field(
                GraphQLFieldDefinition.newFieldDefinition()
                    .name(descriptor.name)
                    .argument(GraphQLArgument.newArgument().name("filter").type(filter).build())
                    .dataFetcher {
                        executeQuery(descriptor, it)
                    }
                    .type(GraphQLList.list(GraphQLTypeReference.typeRef(descriptor.name)))
            )
        } // for

        // mutation

        val mutation = GraphQLObjectType.newObject().name("Mutation")

        for ( descriptor in objectManager.descriptors()) {
            // create

            mutation.field(
                GraphQLFieldDefinition.newFieldDefinition()
                    .name("create${descriptor.name}")
                    .argument(GraphQLArgument.newArgument().name("input").type(GraphQLTypeReference.typeRef(descriptor.name + "Input")).build())
                    .dataFetcher {
                        executeCreate(descriptor, it)
                    }
                    .type(GraphQLTypeReference.typeRef(descriptor.name))
            )

            // update

            mutation.field(
                GraphQLFieldDefinition.newFieldDefinition()
                    .name("update${descriptor.name}")
                    .argument(GraphQLArgument.newArgument().name("input").type(GraphQLTypeReference.typeRef("${descriptor.name}Input")).build())
                    .dataFetcher {
                        executeUpdate(descriptor, it)
                    }
                    .type(GraphQLTypeReference.typeRef(descriptor.name))
            )

            // bulk update

            mutation.field(
                GraphQLFieldDefinition.newFieldDefinition()
                    .name("update${descriptor.name}s")
                    .argument(GraphQLArgument.newArgument().name("where").type(GraphQLTypeReference.typeRef("${descriptor.name}Filter")).build())
                    .argument(GraphQLArgument.newArgument().name("input").type(GraphQLTypeReference.typeRef("${descriptor.name}Input")).build())
                    .dataFetcher {
                        executeBulkUpdate(descriptor, it)
                    }
                    .type(GraphQLList.list(GraphQLTypeReference.typeRef(descriptor.name)))
            )

            // delete

            mutation.field(
                GraphQLFieldDefinition.newFieldDefinition()
                    .name("delete${descriptor.name}s")
                    .argument(GraphQLArgument.newArgument().name("where").type(GraphQLTypeReference.typeRef("${descriptor.name}Filter")).build())
                    .dataFetcher {
                        executeDelete(descriptor, it)
                    }
                    .type(GraphQLTypeReference.typeRef("OperationResult"))
            )
        } // for

        // create schema

        val schema = GraphQLSchema.newSchema()
            .query(query.build())
            .mutation(mutation.build())
            .additionalTypes(types)
            .build()

        println(SchemaPrinter().print(schema))

        return schema
    }

    // private

    private fun executeQuery(descriptor: ObjectDescriptor, environment: DataFetchingEnvironment) : List<DataObject> {
        return queryBuilder.buildQuery(descriptor, environment.getArgument<Any>("filter")).execute().getResultList()
    }

    private fun executeCreate(descriptor: ObjectDescriptor, environment: DataFetchingEnvironment) : DataObject {
        return mutator.create(descriptor, environment.getArgument<Map<String,Any>>("input")!!)
    }

    private fun executeUpdate(descriptor: ObjectDescriptor, environment: DataFetchingEnvironment) : DataObject {
        return mutator.update(descriptor, environment.getArgument<Map<String,Any>>("input")!!)
    }

    private fun executeBulkUpdate(descriptor: ObjectDescriptor, environment: DataFetchingEnvironment) : Array<DataObject> {
        val where = environment.getArgument<Map<String,Any>>("where")
        val input = environment.getArgument<Map<String,Any>>("input")!!

        return mutator.bulkUpdate(queryBuilder.buildQuery(descriptor, where).execute().getResultList(), input)
    }

    private fun executeDelete(descriptor: ObjectDescriptor, environment: DataFetchingEnvironment) : Int {
        val result = queryBuilder.buildQuery(descriptor,  environment.getArgument<Map<String,Any>>("where")).execute().getResultList()

        for ( value in result)
            objectManager.delete(value)

        return result.size
    }

    private fun stringFilter() : GraphQLInputObjectType {
        return GraphQLInputObjectType.newInputObject()
            .name("StringFilter")
            .field(
                GraphQLInputObjectField.newInputObjectField()
                    .name("eq")
                    .type(Scalars.GraphQLString)
            )
            .field(
                GraphQLInputObjectField.newInputObjectField()
                    .name("ne")
                    .type(Scalars.GraphQLString)
            )
            .build()
    }

    private fun booleanFilter() : GraphQLInputObjectType {
        return GraphQLInputObjectType.newInputObject()
            .name("BooleanFilter")
            .field(
                GraphQLInputObjectField.newInputObjectField()
                    .name("eq")
                    .type(Scalars.GraphQLBoolean)
            )
            .field(
                GraphQLInputObjectField.newInputObjectField()
                    .name("ne")
                    .type(Scalars.GraphQLBoolean)
            )
            .build()
    }

    private fun intFilter() : GraphQLInputObjectType {
        return GraphQLInputObjectType.newInputObject()
            .name("IntFilter")
            .field(
                GraphQLInputObjectField.newInputObjectField()
                    .name("lt")
                    .type(Scalars.GraphQLInt)
            )
            .field(
                GraphQLInputObjectField.newInputObjectField()
                    .name("le")
                    .type(Scalars.GraphQLInt)
            )
            .field(
                GraphQLInputObjectField.newInputObjectField()
                    .name("gt")
                    .type(Scalars.GraphQLInt)
            )
            .field(
                GraphQLInputObjectField.newInputObjectField()
                    .name("ge")
                    .type(Scalars.GraphQLInt)
            )
            .field(
                GraphQLInputObjectField.newInputObjectField()
                    .name("eq")
                    .type(Scalars.GraphQLInt)
            )
            .field(
                GraphQLInputObjectField.newInputObjectField()
                    .name("ne")
                    .type(Scalars.GraphQLInt)
            )
            .build()
    }

    private fun floatFilter() : GraphQLInputObjectType {
        return GraphQLInputObjectType.newInputObject()
            .name("FloatFilter")
            .field(GraphQLInputObjectField.newInputObjectField()
                    .name("lt")
                    .type(Scalars.GraphQLFloat)
            )
            .field(GraphQLInputObjectField.newInputObjectField()
                    .name("le")
                    .type(Scalars.GraphQLFloat)
            )
            .field(GraphQLInputObjectField.newInputObjectField()
                    .name("gt")
                    .type(Scalars.GraphQLFloat)
            )
            .field(GraphQLInputObjectField.newInputObjectField()
                    .name("ge")
                    .type(Scalars.GraphQLFloat)
            )
            .field(GraphQLInputObjectField.newInputObjectField()
                    .name("eq")
                    .type(Scalars.GraphQLFloat)
            )
            .field(GraphQLInputObjectField.newInputObjectField()
                    .name("ne")
                    .type(Scalars.GraphQLFloat)
            )
            .build()
    }

    private fun <T:GraphQLType>type4(clazz : Class<*>) : T {
        return when ( clazz ) {
            Boolean::class.javaObjectType  -> Scalars.GraphQLBoolean
            Int::class.javaObjectType  -> Scalars.GraphQLInt
            Short::class.javaObjectType  -> Scalars.GraphQLInt
            Long::class.javaObjectType -> Scalars.GraphQLInt
            Float::class.javaObjectType -> Scalars.GraphQLFloat
            Double::class.javaObjectType -> Scalars.GraphQLFloat
            String::class.javaObjectType  -> Scalars.GraphQLString
            else -> {
                throw Error("unsupported type ${clazz}")
            }
        } as T
    }
}