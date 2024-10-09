package org.sirius.dorm.graphql
/*
* @COPYRIGHT (C) 2023 Andreas Ernst
*
* All rights reserved
*/

import graphql.GraphQL
import graphql.execution.SimpleDataFetcherExceptionHandler
import graphql.schema.idl.RuntimeWiring
import jakarta.annotation.PostConstruct
import org.sirius.dorm.ObjectManager
import org.sirius.dorm.graphql.scalars.LocalDateTimeScalar
import org.sirius.dorm.graphql.test.TestData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class GraphQLProvider {
    // instance data

    @Autowired
    lateinit var test : TestData

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

    @Bean
    fun graphQL(): GraphQL {
        return graphQL
    }

    @PostConstruct
    fun setup() {
        RuntimeWiring.newRuntimeWiring().scalar(LocalDateTimeScalar.INSTANCE)

        graphQL= GraphQL.newGraphQL(withTransaction { SchemaBuilder(objectManager).createSchema() })
            .queryExecutionStrategy(TransactionalExecutionStrategy(objectManager, SimpleDataFetcherExceptionHandler()))
            .mutationExecutionStrategy(TransactionalExecutionStrategy(objectManager, SimpleDataFetcherExceptionHandler()))
            .build()

        queryBuilder = QueryBuilder(objectManager)
    }
}