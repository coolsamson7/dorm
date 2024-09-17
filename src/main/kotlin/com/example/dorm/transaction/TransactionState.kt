package com.example.dorm.transaction
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.example.dorm.ObjectManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.DefaultTransactionDefinition

class TransactionState(val objectManager: ObjectManager, val transactionManager: PlatformTransactionManager) {
    // instance data

    val states = ArrayList<ObjectState>()
    val status : TransactionStatus

    init {
        val def = DefaultTransactionDefinition()

        def.setName("TX")
        def.propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRED

        status = transactionManager.getTransaction(def)
    }

    // TX

    fun commit() {
        transactionManager.commit(status)
    }

    fun rollback() {
        transactionManager.rollback(status)
    }

    // public

    fun register(state: ObjectState) {
        states.add(state)
    }
}