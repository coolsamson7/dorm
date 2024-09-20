package com.quasar.dorm.transaction
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.DataObject
import com.quasar.dorm.ObjectManager
import com.quasar.dorm.model.ObjectDescriptor
import com.quasar.dorm.persistence.DataObjectMapper
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.DefaultTransactionDefinition

abstract class Operation() {
    abstract fun execute()
}

class TransactionState(val objectManager: ObjectManager, val transactionManager: PlatformTransactionManager) {
    // instance data

    val states = HashMap<Int, ObjectState>()
    val status : TransactionStatus

    val pendingOperations = ArrayList<Operation>()

    init {
        val def = DefaultTransactionDefinition()

        def.setName("TX")
        def.propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRED

        status = transactionManager.getTransaction(def)
    }

    fun addOperation(operation: Operation) {
        pendingOperations.add(operation)
    }

    private fun executeOperations() {
        for ( operation in pendingOperations)
            operation.execute()

        pendingOperations.clear()

    }
    // TX

    fun commit(mapper: DataObjectMapper) {
        // commit changes

        for (state in states.values) {
            when ( state.status) {
                Status.CREATED ->  mapper.create(this, state.obj)
                Status.DELETED -> mapper.delete(this, state.obj)
                Status.MANAGED -> {
                    if ( state.isDirty())
                        mapper.update(this, state.obj)
                }
            }
        }

        // execute pending operations

        executeOperations()

        // and commit tx

        transactionManager.commit(status)
    }

    fun rollback(mapper: DataObjectMapper) {
        for (state in states.values) {
            when ( state.status) {
                Status.MANAGED -> if ( state.isDirty()) state.rollback()

                else  -> {} ; // noop
            }
        }

        transactionManager.rollback(status)
    }

    // public

    fun retrieve(id: Int, ifMissing: () -> DataObject) : DataObject {
        return states.getOrPut(id) { ObjectState(ifMissing(), Status.MANAGED) }.obj
    }

    fun register(state: ObjectState) {
        states.put(state.obj.id, state)
    }

    fun create(obj: DataObject) {
        val state = ObjectState(obj, Status.MANAGED) // as soon as it is flushed it will be managed

        // force flush

        objectManager.mapper.create(this, obj)

        states.put(obj.id, state)
    }

    fun flush(objectDescriptor: ObjectDescriptor) {
        for ( state in states.values) {
            if ( state.obj.type == objectDescriptor) {
                when ( state.status) {
                    Status.CREATED ->  objectManager.mapper.create(this, state.obj)
                    Status.DELETED -> objectManager.mapper.delete(this, state.obj)
                    Status.MANAGED -> {
                        if ( state.isDirty())
                            objectManager.mapper.update(this, state.obj)
                    }
                }
            }
        } // for

        executeOperations()
    }
}