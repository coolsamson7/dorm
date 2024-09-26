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
import com.fasterxml.jackson.databind.ObjectMapper
import org.sirius.common.tracer.TraceLevel
import org.sirius.common.tracer.Tracer
import org.sirius.dorm.`object`.DataObject
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.sirius.dorm.ObjectManager
import org.sirius.dorm.transaction.Status
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class DataObjectMapper() {
    // instance data

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var mapper: ObjectMapper

    private val reader = ConcurrentHashMap<String, ObjectReader>()
    private val jsonReader = ConcurrentHashMap<String, JSONReader>()
    private val writer = ConcurrentHashMap<String, ObjectWriter>()
    private val updater = ConcurrentHashMap<String, ObjectUpdater>()

    // public

    fun update(state: TransactionState, obj: DataObject) {
        if ( Tracer.ENABLED)
            Tracer.trace("sirius.sirius.dorm", TraceLevel.HIGH, "update %s[%d]", obj.type.name, obj.id)

        updater4(obj.type).update(state, obj)

         // update entity

        obj.entity!!.json = ""//TODO ? mapper.writeValueAsString(obj)
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

        writer4(obj.type).write(state, obj, entityManager) // will create the attribute entities
    }


    fun readFromEntity(state: TransactionState, objectDescriptor: ObjectDescriptor, entity: EntityEntity) : DataObject {
        return state.retrieve(entity.id) {
            // read json

            val node = mapper.readTree(entity.json)

            val obj = jsonReader4(objectDescriptor).read(node)

            obj.id = entity.id

            // done

            return@retrieve obj
        }
    }

    fun read(state: TransactionState, objectDescriptor: ObjectDescriptor, entity: EntityEntity): DataObject {
        return state.retrieve(entity.id) {
            if ( Tracer.ENABLED)
                Tracer.trace("com.sirius.dorm", TraceLevel.HIGH, "read %s", objectDescriptor.name)

            val obj = objectDescriptor.create(Status.MANAGED)

            val id =  entity.id
            obj.entity = entity
            obj["id"] = id

            val reader = reader4(objectDescriptor, state.objectManager)

            for ( attribute in entity.properties)
                reader.read(obj, objectDescriptor.property(attribute.attribute), attribute)

            // done

            return@retrieve obj
        }
    }

    fun read(state: TransactionState, objectDescriptor: ObjectDescriptor, attributes: List<PropertyEntity>, start: Int, end: Int) : DataObject {
        return state.retrieve(attributes[start].entity.id) {
            if ( Tracer.ENABLED)
                Tracer.trace("com.sirius.dorm", TraceLevel.HIGH, "read %s[%d]", objectDescriptor.name,  attributes[start].entity.id)

            val obj = objectDescriptor.create(Status.MANAGED)

            obj.entity = attributes[start].entity
            val id =  obj.entity!!.id

            obj.id = id

            val reader = reader4(objectDescriptor, state.objectManager)

            for ( i in start..end) {
                val attribute = attributes[i]

                reader.read(obj, objectDescriptor.property(attribute.attribute), attribute)
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

    private fun writer4(objectDescriptor: ObjectDescriptor) : ObjectWriter {
        return writer.getOrPut(objectDescriptor.name) { -> ObjectWriter(objectDescriptor) }
    }

    private fun updater4(objectDescriptor: ObjectDescriptor) : ObjectUpdater {
        return updater.getOrPut(objectDescriptor.name) { ObjectUpdater(objectDescriptor) }
    }


    fun clear() {
        updater.clear()
    }
}