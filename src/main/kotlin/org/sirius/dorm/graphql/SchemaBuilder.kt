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

    // public

    fun createSchema() : GraphQLSchema? {
        val types = HashSet<GraphQLType>()

        // common input

        val stringFilter = stringFilter()
        val intFilter = intFilter()

        // iterate over object definitions

        for ( descriptor in objectManager.descriptors()) {
            val newObject = GraphQLObjectType.newObject().name(descriptor.name)

            for ( field in descriptor.properties) {
                if ( field.isAttribute())
                    newObject.field(
                        GraphQLFieldDefinition.newFieldDefinition()
                            .name(field.name)
                            .dataFetcher {
                                it.getSource<DataObject>()!!.get(it.field.name)
                            }
                            .type(type4(field.asAttribute().baseType()))
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
            } // for

            types.add(newObject.build())
        }

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

    private fun type4(clazz : Class<*>) : GraphQLOutputType {
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
        }
    }

}