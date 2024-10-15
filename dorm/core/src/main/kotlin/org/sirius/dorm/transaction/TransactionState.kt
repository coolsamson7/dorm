package org.sirius.dorm.transaction
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.`object`.DataObject
import org.sirius.dorm.`object`.Relation
import org.sirius.dorm.persistence.DataObjectMapper
import org.sirius.dorm.persistence.entity.EntityEntity
import org.sirius.dorm.persistence.entity.EntitySchemaEntity
import org.sirius.dorm.persistence.entity.EntityStatus
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.DefaultTransactionDefinition
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


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
    private val operations = LinkedList<UpdateRelation>()

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
    val timestamp = LocalDateTime.now()

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

    private fun markOrphans() {
        val objects = states.values.filter { state -> state.isSet(Status.MODIFIED) && !state.isSet(Status.CREATED) }

        for ( state in objects) {
            val obj = state.obj
            for ( property in obj.type.properties)
                if ( property.isRelation()) {
                    val relation = property.asRelation()

                    if (relation.inverseRelation!!.removeOrphans) {
                        val target = obj.value<Any>(relation.index)

                        if ( target === null)
                            state.set(Status.DELETED)
                    } // if
                } // if
        } // for
    }

    private fun processDeletedObjects() {
        // this is the set of initially deleted objects

        val entities = states.values
            .filter { state -> state.isSet(Status.DELETED) }
            .map { state -> state.obj.entity!! }
            .toMutableSet()

        val queue : MutableList<EntityEntity> = entities.toMutableList()
        entities.clear()

        while (queue.isNotEmpty()) {
            val entity = queue.removeAt(0);

            if (!entities.contains(entity)) {
                entities.add(entity)

                if (states.containsKey(entity.id)) {
                    // mark it as deleted anyway

                    val state = states[entity.id]!!

                    state.set(Status.DELETED)

                    // check cascading relations

                    val obj = state.obj

                    var i = 0
                    for (property in obj.type.properties) {
                        if (property.isRelation() && property.asRelation().cascadeDelete) {
                            val relation = obj.relation(i)

                            for (target in relation.relations())
                                queue.add(target.entity)
                        } // if

                        i++
                    } // for
                }
                else {
                    // object is not loaded, rely on the db only

                    val type = objectManager.getDescriptor(entity.type)

                    for (property in type.properties) {
                        if (property.isRelation() && property.asRelation().cascadeDelete) {
                            val propertyEntity = entity.properties.first { propertyEntity -> propertyEntity.attribute == property.name }

                            for ( targetProperty in if ( property.asRelation().isOwner()) propertyEntity.targets else propertyEntity.sources)
                                queue.add(targetProperty.entity)
                        } // if
                    } // for
                }
            } // if
        } // while

        // finally delete

        val entityManager = objectManager.entityManager
        for ( entity in entities) {
            for ( property in entity.properties) {
                if ( property.targets.size > 0 ) {

                    for ( target in property.targets)
                        target.sources.remove(property)

                    property.targets.clear()
                }

                if ( property.sources.size > 0 ) {
                    for ( target in property.sources)
                        target.targets.remove(property)

                    property.sources.clear()
                }
            }

            entityManager.remove(entity)
        }
    }

    // TX

    fun commit(mapper: DataObjectMapper) {
        try {
            // mark orphans first

            markOrphans()

            // recursively delete objects...

            processDeletedObjects()

            // commit changes

            for (state in states.values) {
                if ( state.isSet(Status.DELETED)) {
                    // noop!
                }

                else if ( state.isSet(Status.CREATED))
                    mapper.create(this, state.obj)

                else if ( state.isSet(Status.MODIFIED))
                    if (state.isDirty())
                        mapper.update(this, state.obj)
            } // for

            // execute pending operations

            executeOperations() // TODO deleted!

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
            if ( state.isSet(Status.MANAGED)) {
                if ( state.isDirty()) state.rollback()
            }
        }

        transactionManager.rollback(status)
    }

    // public

    fun retrieve(id: Long, ifMissing: () -> DataObject) : DataObject {
        return states.getOrPut(id) { ifMissing().state }.obj
    }

    fun register(state: ObjectState) {
        states.put(state.obj.id, state)
    }

    private fun createState() : EntityStatus {
        return EntityStatus(
            timestamp,
            objectManager.sessionContext.getUser(),
            timestamp,
        "")
    }

    fun create(obj: DataObject) : DataObject {
        obj.entity = EntityEntity(0, obj.type.name, 0, createState(), ArrayList())

        this.objectManager.entityManager.persist(obj.entity)

        obj["id"] = obj.entity!!.id
        obj["versionCounter"] = obj.entity!!.versionCounter
        obj["status"] = obj.entity!!.status

        states.put(obj.id, obj.state)

        return obj
    }

    fun flush(objectDescriptor: ObjectDescriptor) {
        for ( state in states.values) {
            if ( state.obj.type == objectDescriptor) {
                if ( state.isSet(Status.CREATED))
                    objectManager.mapper.create(this, state.obj)

                else if ( state.isSet(Status.DELETED))
                    objectManager.mapper.delete(this, state.obj)

                else if ( state.isSet(Status.MANAGED)) {
                    if ( state.isDirty())
                        objectManager.mapper.update(this, state.obj)
                    }
                }
        } // for

        executeOperations()
    }

    // callbacks

    fun onPrePersist(entityEntity: EntityEntity) {
        val status = entityEntity.status!!

        status.created = timestamp
        status.createdBy = objectManager.sessionContext.getUser()
    }

    fun onPreUpdate(entityEntity: EntitySchemaEntity) {
        val status = entityEntity.status!!

        status.modified = timestamp
        status.modifiedBy = objectManager.sessionContext.getUser()
    }

    fun onPrePersist(entityEntity: EntitySchemaEntity) {
        val status = entityEntity.status!!

        status.created = timestamp
        status.createdBy = objectManager.sessionContext.getUser()
    }

    fun onPreUpdate(entityEntity: EntityEntity) {
        val status = entityEntity.status!!

        status.modified = timestamp
        status.modifiedBy = objectManager.sessionContext.getUser()
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