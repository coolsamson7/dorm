package org.sirius.dorm.transaction
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.Cascade
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.`object`.DataObject
import org.sirius.dorm.`object`.Relation
import org.sirius.dorm.persistence.DataObjectMapper
import org.sirius.dorm.persistence.entity.EntityEntity
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.DefaultTransactionDefinition
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet


abstract class Operation() {
    abstract fun execute()
}

data class RedoKey(val id : Long, val relation: String) {}

abstract class UpdateRelation {
    abstract fun execute(relation: Relation)
}

class AddToRelation(val obj: DataObject) : UpdateRelation() {
    override fun execute(relation: Relation) {
        relation.addedToInverse(obj)
    }
}

class RemoveFromRelation(val obj: DataObject) : UpdateRelation() {
    override fun execute(relation: Relation) {
        relation.removedFromInverse(obj)
    }
}
class Redo() {
    val operations = LinkedList<UpdateRelation>()

    fun add(update: UpdateRelation) {
        operations.add(update)
    }

    fun execute(relation: Relation) {
        for ( operation in operations)
            operation.execute(relation)
    }
}

class TransactionState(val objectManager: ObjectManager, val transactionManager: PlatformTransactionManager) {
    // instance data

    private val states = HashMap<Long, ObjectState>()
    val status : TransactionStatus

    private val pendingOperations = ArrayList<Operation>()
    private val redos = HashMap<RedoKey, Redo> ()

    init {
        val def = DefaultTransactionDefinition()

        def.setName("TX")
        def.propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRED

        status = transactionManager.getTransaction(def)
    }

    fun addRedo(id: Long, relation: String, redo: UpdateRelation) {
        val key = RedoKey(id, relation)
        redos.getOrPut(key, { Redo() }).add(redo)
    }

    fun checkRedos(id: Long, relationName: String, relation: Relation) {
        val key = RedoKey(id, relationName)
        val redo = redos[key]
        if ( redo !== null) {
            redo.execute(relation)

            redos.remove(key)
        } // if
    }

    fun addOperation(operation: Operation) {
        pendingOperations.add(operation)
    }

    private fun executeOperations() {
        for ( operation in pendingOperations)
            operation.execute()

        pendingOperations.clear()

    }

    // TODO do it in waves
    fun processDeletedObjects() {
        val objects = states.values.filter { state -> state.status == Status.DELETED }

        // this is the set of deleted objects

        val ids = HashSet<Long>()

        val queue : MutableList<Long> = objects.map { state -> state.obj.id}.toMutableList()
        while ( queue.isNotEmpty()) {
            val id = queue.removeAt(0);

            if (!ids.contains(id)) {
                ids.add(id)

                if (states.containsKey(id)) {
                    val obj = states[id]!!.obj

                    obj.state?.status = Status.DELETED

                    var i = 0
                    for (property in obj.type.properties) {
                        if (!property.isAttribute() && property.asRelation().cascade == Cascade.DELETE) {
                            val relation = obj.relation(i)

                            if (relation.isLoaded()) {
                                for (r in relation.relations())
                                    queue.add(r.entity.id)
                            }
                            else { // TODO at least prepared statement
                                objectManager.jdbcTemplate.query<Long>("SELECT DISTINCT TO_ENTITY FROM RELATIONS WHERE FROM_ENTITY = ${id} AND FROM_ATTR='${property.name}'") { rs, _ ->
                                    rs.getLong("TO_ENTITY")
                                }.forEach { id -> queue.add(id) }
                            }
                        } // if

                        i++
                    } // for
                } // if
            } // if
        } // while

        // finally delete

        val entityManager = objectManager.entityManager

        val criteriaBuilder = entityManager.getCriteriaBuilder()

        val criteriaQuery = criteriaBuilder.createQuery(EntityEntity::class.java)

        val entity = criteriaQuery.from(EntityEntity::class.java)
        criteriaQuery.where( criteriaBuilder.isTrue(entity.get<Long>("id").`in`(*ids.toTypedArray())))

         for ( e in entityManager.createQuery(criteriaQuery).resultList)
             entityManager.remove(e)
    }

    // TX

    fun commit(mapper: DataObjectMapper) {
        try {
            processDeletedObjects()

            // commit changes

            for (state in states.values) {
                when (state.status) {
                    Status.CREATED -> mapper.create(this, state.obj)
                    Status.DELETED -> { /* already processed */
                    }//mapper.delete(this, state.obj)
                    Status.MANAGED -> {
                        if (state.isDirty())
                            mapper.update(this, state.obj)
                    }
                }
            }

            // execute pending operations

            executeOperations()

            // and commit tx

            transactionManager.commit(status)
        }
        catch(exception: Throwable) {
            transactionManager.rollback(status)

            throw exception
        }
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

    fun retrieve(id: Long, ifMissing: () -> DataObject) : DataObject {
        return states.getOrPut(id) { ObjectState(ifMissing(), Status.MANAGED) }.obj
    }

    fun register(state: ObjectState) {
        states.put(state.obj.id, state)
    }

    fun create(obj: DataObject) : DataObject {
        val state = ObjectState(obj, Status.CREATED)

        obj.entity = EntityEntity(0, obj.type.name, "{}", ArrayList())

        this.objectManager.entityManager.persist(obj.entity)

        obj["id"] = obj.entity!!.id

        states.put(obj.id, state)

        return obj
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

    companion object {
        private val current = ThreadLocal<TransactionState>()

        fun current() : TransactionState {
            return current.get()
        }

        fun set(objectManager: ObjectManager, transactionManager: PlatformTransactionManager) {
            current.set(TransactionState(objectManager, transactionManager))
        }

        fun remove() {
            current.remove()
        }
    }
}