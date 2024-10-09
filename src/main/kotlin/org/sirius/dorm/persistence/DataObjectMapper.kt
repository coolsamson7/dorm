package org.sirius.dorm.persistence
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.json.JSONReader
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.persistence.entity.PropertyEntity
import org.sirius.dorm.persistence.entity.EntityEntity
import org.sirius.dorm.transaction.TransactionState
import org.sirius.common.tracer.TraceLevel
import org.sirius.common.tracer.Tracer
import org.sirius.dorm.`object`.DataObject
import jakarta.persistence.EntityManager
import jakarta.persistence.LockModeType
import jakarta.persistence.PersistenceContext
import org.sirius.dorm.ObjectManager
import org.sirius.dorm.persistence.entity.EntityStatus
import org.sirius.dorm.transaction.Status
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class DataObjectMapper() {
    // instance data

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val reader = ConcurrentHashMap<String, ObjectReader>()
    private val jsonReader = ConcurrentHashMap<String, JSONReader>()
    private val writer = ConcurrentHashMap<String, ObjectCreator>()
    private val updater = ConcurrentHashMap<String, ObjectUpdater>()

    // public

    fun update(state: TransactionState, obj: DataObject) {
        if ( Tracer.ENABLED)
            Tracer.trace("sirius.sirius.dorm", TraceLevel.HIGH, "update %s[%d]", obj.type.name, obj.id)

        updater4(obj.type).update(state, obj)

         // touch entity!

        obj.entity!!.status!!.modified = state.timestamp
        //obj.entity!!.status!!.modifiedBy = state.timestamp

        //obj.entity!!.status = EntityStatus.from(obj.entity!!.status!!)
    }

    fun delete(state: TransactionState, obj: DataObject) {
        if ( Tracer.ENABLED)
            Tracer.trace("com.sirius.dorm", TraceLevel.HIGH, "delete %s[%d]", obj.type.name, obj.id)

        entityManager.remove(obj.entity!!) // will delete properties

        // what about aggregated objects
    }

    fun create(state: TransactionState, obj: DataObject) {
        if ( Tracer.ENABLED)
            Tracer.trace("com.sirius.dorm", TraceLevel.HIGH, "create %s[%d]", obj.type.name, obj.entity!!.id)

        writer4(obj.type).create(state, obj, entityManager) // will create the attribute entities
    }

    fun read(state: TransactionState, objectDescriptor: ObjectDescriptor, entity: EntityEntity): DataObject {
        return state.retrieve(entity.id) {
            if ( Tracer.ENABLED)
                Tracer.trace("com.sirius.dorm", TraceLevel.HIGH, "read %s", objectDescriptor.name)

            val obj = objectDescriptor.create(Status.MANAGED)

            entityManager.lock(entity, LockModeType.OPTIMISTIC)

            obj.entity = entity

            val reader = reader4(objectDescriptor, state.objectManager)

            // read from entity

            reader.readEntity(obj, entity)

            // read from properties

            for ( property in entity.properties)
                reader.read(obj, objectDescriptor.property(property.attribute), property, entity)

            // done

            return@retrieve obj
        }
    }

    fun read(state: TransactionState, objectDescriptor: ObjectDescriptor, properties: List<PropertyEntity>, start: Int, end: Int) : DataObject {
        return state.retrieve(properties[start].entity.id) {
            if ( Tracer.ENABLED)
                Tracer.trace("com.sirius.dorm", TraceLevel.HIGH, "read %s[%d]", objectDescriptor.name,  properties[start].entity.id)

            val obj = objectDescriptor.create(Status.MANAGED)

            val entity = properties[start].entity
            obj.entity = entity

            // lock optimistically

            entityManager.lock(entity, LockModeType.OPTIMISTIC)

            val reader = reader4(objectDescriptor, state.objectManager)

            // read stuff from entity

            reader.readEntity(obj, entity)

            // read properties

            for ( i in start..end) {
                val property = properties[i]

                reader.read(obj, objectDescriptor.property(property.attribute), property, entity)
            }

            // done

            return@retrieve obj
        }
    }

    // private

    private fun reader4(objectDescriptor: ObjectDescriptor, objectManager: ObjectManager) : ObjectReader {
        return reader.getOrPut(objectDescriptor.name) { -> ObjectReader(objectDescriptor, objectManager) }
    }

    private fun jsonReader4(objectDescriptor: ObjectDescriptor) : JSONReader {
        return jsonReader.getOrPut(objectDescriptor.name) { -> JSONReader(objectDescriptor) }
    }

    private fun writer4(objectDescriptor: ObjectDescriptor) : ObjectCreator {
        return writer.getOrPut(objectDescriptor.name) { -> ObjectCreator(objectDescriptor) }
    }

    private fun updater4(objectDescriptor: ObjectDescriptor) : ObjectUpdater {
        return updater.getOrPut(objectDescriptor.name) { ObjectUpdater(objectDescriptor) }
    }

    fun clear() {
        updater.clear()
    }
}