package com.quasar.dorm.transaction
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.DataObject
import com.quasar.dorm.ObjectManager
import com.quasar.dorm.model.ObjectDescriptor
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.DefaultTransactionDefinition

class TransactionState(val objectManager: ObjectManager, val transactionManager: PlatformTransactionManager) {
    // instance data

    val states = HashMap<Int, ObjectState>()
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

    fun retrieve(id: Int, ifMissing: () -> DataObject) : DataObject {
        return states.getOrPut(id) { ObjectState(ifMissing(), Status.MANAGED) }.obj
    }

    fun register(state: ObjectState) {
        states.put(state.obj.id, state)
    }

    fun flush(objectDescriptor: ObjectDescriptor) {
        for ( state in states.values) {
            if ( state.obj.type == objectDescriptor) {
                when ( state.status) {
                    Status.CREATED ->  objectManager.mapper.create(state.obj)
                    Status.DELETED -> objectManager.mapper.delete(state.obj)
                    Status.MANAGED -> {
                        if ( state.isDirty())
                            objectManager.mapper.update(state.obj)
                    }
                }
            }
        }
    }
}