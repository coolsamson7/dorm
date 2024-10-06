package org.sirius.dorm.graphql
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import graphql.ExecutionResult
import graphql.execution.AsyncExecutionStrategy
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.ExecutionContext
import graphql.execution.ExecutionStrategyParameters
import org.sirius.dorm.ObjectManager
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