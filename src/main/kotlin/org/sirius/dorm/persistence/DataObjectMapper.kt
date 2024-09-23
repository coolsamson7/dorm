package org.sirius.dorm.persistence
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.json.JSONReader
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.persistence.entity.AttributeEntity
import org.sirius.dorm.persistence.entity.EntityEntity
import org.sirius.dorm.transaction.TransactionState
import com.fasterxml.jackson.databind.ObjectMapper
import org.sirius.common.tracer.TraceLevel
import org.sirius.common.tracer.Tracer
import org.sirius.dorm.`object`.DataObject
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
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
    private val updater = ConcurrentHashMap<String, AttributeUpdater<Any>>()
    private val deleter = ConcurrentHashMap<String, ObjectDeleter>()

    // public

    fun update(state: TransactionState, obj: DataObject) {
        if ( Tracer.ENABLED)
            Tracer.trace("sirius.sirius.dorm", TraceLevel.HIGH, "update %s[%d]", obj.type.name, obj.id)

        val builder = entityManager.criteriaBuilder

        val objectManager = state.objectManager
        val snapshot = obj.state!!.snapshot!!

        // update attributes

        val properties = obj.type.properties
        for ( index in 1..<obj.values.size) {
            if ( obj.values[index].isDirty(snapshot[index])) {
                val property = properties[index]

                if ( property.isAttribute())
                    when ( property.asAttribute().baseType()) {
                        String::class.java -> updater4<String>("stringValue",  String::class.java).update(obj.id, property.name, obj.values[index].get(objectManager) as String)
                        Short::class.java -> updater4<Int>("intValue",  Short::class.java).update(obj.id, property.name, (obj.values[index].get(objectManager) as Short).toInt())
                        Int::class.java -> updater4<Int>("intValue",  Int::class.java).update(obj.id, property.name, obj.values[index].get(objectManager) as Int)
                        Integer::class.java -> updater4<Int>("intValue",  Integer::class.java).update(obj.id, property.name, obj.values[index].get(objectManager) as Int)
                        Long::class.java -> updater4<Int>("intValue",  Long::class.java).update(obj.id, property.name, (obj.values[index].get(objectManager) as Long).toInt())
                        Float::class.java -> updater4<Double>("doubleValue",  Float::class.java).update(obj.id, property.name, (obj.values[index].get(objectManager) as Float).toDouble())
                        Double::class.java -> updater4<Double>("doubleValue",  Double::class.java).update(obj.id, property.name, obj.values[index].get(objectManager) as Double)
                        Boolean::class.java -> updater4<Int>("intValue",  Integer::class.java).update(obj.id, property.name, if (obj.values[index].get(objectManager) as Boolean) 1 else 0)
                        Boolean::class.javaObjectType -> updater4<Int>("intValue",  Integer::class.java).update(obj.id, property.name, if (obj.values[index].get(objectManager) as Boolean) 1 else 0)

                        else -> {
                                throw Error("usupported type ${property.asAttribute().baseType().simpleName}")
                        }
                    }
                else {
                    obj.values[index].flush()
                }
            }
        } // for

        // update entity

        val json = mapper.writeValueAsString(obj)

        val entityCriteriaQuery = builder.createCriteriaUpdate(EntityEntity::class.java)
        val entityFrom = entityCriteriaQuery.from(EntityEntity::class.java)
//TODO remove
        entityCriteriaQuery
            .set("json", json)
            .where(builder.equal(entityFrom.get<Int>("id"), obj.id))

        entityManager.createQuery(entityCriteriaQuery).executeUpdate()
    }

    fun delete(state: TransactionState, obj: DataObject) {
        if ( Tracer.ENABLED)
            Tracer.trace("com.sirius.dorm", TraceLevel.HIGH, "delete %s[%d]", obj.type.name, obj.id)


        entityManager.remove(obj.entity!!)
    }

    fun create(state: TransactionState, obj: DataObject) {
        val descriptor = obj.type

        obj.entity = EntityEntity(0, descriptor.name, mapper.writeValueAsString(obj))

        entityManager.persist(obj.entity) // we need the id...is that required, think of a lifecycle hook?

        if ( Tracer.ENABLED)
            Tracer.trace("com.sirius.dorm", TraceLevel.HIGH, "create %s[%d]", obj.type.name, obj.entity!!.id)


        writer4(descriptor).write(state, obj, entityManager) // will create the attribute entities

        // set as value as well

        obj.id = obj.entity!!.id
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

            val obj = objectDescriptor.create()

            val id =  entity.id
            obj.entity = entity
            obj["id"] = id

            val reader = reader4(objectDescriptor)

            for ( attribute in entity.properties)
                reader.read(obj, objectDescriptor.property(attribute.attribute), attribute)

            // done

            return@retrieve obj
        }
    }

    fun read(state: TransactionState, objectDescriptor: ObjectDescriptor, attributes: List<AttributeEntity>, start: Int, end: Int) : DataObject {
        return state.retrieve(attributes[start].entity.id) {
            if ( Tracer.ENABLED)
                Tracer.trace("com.sirius.dorm", TraceLevel.HIGH, "read %s[%d]", objectDescriptor.name,  attributes[start].entity.id)

            val obj = objectDescriptor.create()

            obj.entity = attributes[start].entity
            val id =  obj.entity!!.id

            obj.id = id

            val reader = reader4(objectDescriptor)

            for ( i in start..end) {
                val attribute = attributes[i]

                reader.read(obj, objectDescriptor.property(attribute.attribute), attribute)
            }

            // done

            return@retrieve obj
        }
    }

    // private

    private fun reader4(objectDescriptor: ObjectDescriptor) : ObjectReader {
        return reader.getOrPut(objectDescriptor.name) { -> ObjectReader(objectDescriptor) }
    }

    private fun jsonReader4(objectDescriptor: ObjectDescriptor) : JSONReader {
        return jsonReader.getOrPut(objectDescriptor.name) { -> JSONReader(objectDescriptor) }
    }

    private fun writer4(objectDescriptor: ObjectDescriptor) : ObjectWriter {
        return writer.getOrPut(objectDescriptor.name) { -> ObjectWriter(objectDescriptor) }
    }

    private fun <T> updater4(attribute: String, type: Class<*>) : AttributeUpdater<T> {
        return updater.getOrPut(attribute) { AttributeUpdater(attribute, type as Class<Any>, entityManager) } as AttributeUpdater<T>
    }

    private fun deleter4(objectDescriptor: ObjectDescriptor) : ObjectDeleter {
        return deleter.getOrPut(objectDescriptor.name) { ObjectDeleter(entityManager) }
    }

    fun clear() {
        updater.clear()
        deleter.clear()
    }
}