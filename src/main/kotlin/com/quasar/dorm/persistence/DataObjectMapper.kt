package com.quasar.dorm.persistence
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.DataObject
import com.quasar.dorm.json.JSONReader
import com.quasar.dorm.model.ObjectDescriptor
import com.quasar.dorm.persistence.entity.AttributeEntity
import com.quasar.dorm.persistence.entity.EntityEntity
import com.quasar.dorm.transaction.ObjectState
import com.quasar.dorm.transaction.Status
import com.quasar.dorm.transaction.TransactionState
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.Query
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.ParameterExpression
import jakarta.persistence.criteria.Root
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

typealias PropertyReader = (obj: DataObject, property: Int, attribute: AttributeEntity) -> Unit
typealias PropertyWriter = (obj: DataObject, property: Int, attribute: AttributeEntity) -> Unit

class ObjectReader(descriptor: ObjectDescriptor) {
    // instance data

    private val reader: Array<PropertyReader> =
        descriptor.properties
            .filter { property -> !property.isPrimaryKey }
            .map { property -> reader4(property.type.baseType) }.toTypedArray()

    // public

    fun read(obj: DataObject, property: Int, attribute: AttributeEntity) {
        reader[property-1](obj, property, attribute)
    }

    // companion

    companion object {
        fun valueReader(clazz: Class<Any>) :  (attribute: AttributeEntity) -> Any {
            return when (clazz) {
                Boolean::class.java -> { attribute: AttributeEntity -> attribute.intValue == 1 }

                Short::class.java -> { attribute: AttributeEntity -> attribute.intValue.toShort() }

                Integer::class.java -> { attribute: AttributeEntity -> attribute.intValue }

                Long::class.java -> { attribute: AttributeEntity -> attribute.intValue.toLong() }

                Float::class.java -> { attribute: AttributeEntity -> attribute.doubleValue.toFloat() }

                Double::class.java -> { attribute: AttributeEntity -> attribute.doubleValue }

                String::class.java -> { attribute: AttributeEntity -> attribute.stringValue }

                else -> throw Error("unsupported type")
            }
        }

        fun reader4(clazz: Class<Any>): PropertyWriter {
            return when (clazz) {
                Boolean::class.java -> { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    obj.values[property] = attribute.intValue == 1
                }

                String::class.java -> { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    obj.values[property] = attribute.stringValue
                }

                Short::class.java -> { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    obj.values[property] = attribute.intValue.toShort()
                }

                Integer::class.java -> { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    obj.values[property] = attribute.intValue
                }

                Long::class.java -> { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    obj.values[property] = attribute.intValue.toLong()
                }

                Float::class.java -> { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    obj.values[property] = attribute.doubleValue.toFloat()
                }

                Double::class.java -> { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    obj.values[property] = attribute.doubleValue
                }

                else -> { _: DataObject, _: Int, _: AttributeEntity -> throw Error("unsupported type") }

            }
        }
    }
}

class ObjectWriter(private val descriptor: ObjectDescriptor) {
    // instance data

    private val writer: Array<PropertyWriter> = descriptor.properties
        .filter { property -> !property.isPrimaryKey }
        .map { property -> writer4(property.type.baseType)}.toTypedArray()

    // public

    fun update(obj: DataObject, property: Int, attribute: AttributeEntity) {
        writer[property-1](obj, property, attribute)
    }

    fun write(obj: DataObject, entityManager: EntityManager) {
        var i = 1
        val id = obj.getId()
        for ( writer in writer) {
            val attribute = AttributeEntity(id, descriptor.properties[i].name, descriptor.name, "", 0, 0.0)

            writer(obj, i++, attribute)

            entityManager.persist(attribute)
        }
    }

    // companion

    companion object {
        fun writer4(clazz: Class<Any>) : PropertyWriter {
            return when (clazz) {
                Boolean::class.java -> { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    attribute.intValue = if ( obj.values[property] as Boolean) 1 else 0
                }

                String::class.java -> { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    attribute.stringValue = obj.values[property] as String
                }

                Short::class.java ->   { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    attribute.intValue = (obj.values[property] as Number).toInt()
                }

                Integer::class.java ->   { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    attribute.intValue = obj.values[property] as Int
                }

                Long::class.java ->   { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    attribute.intValue = (obj.values[property] as Number).toInt()
                }

                Float::class.java ->   { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    attribute.doubleValue = (obj.values[property] as Number).toDouble()
                }

                Double::class.java ->   { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    attribute.doubleValue = obj.values[property] as Double
                }

                else -> { _: DataObject, _: Int, _: AttributeEntity -> throw Error("unsupported type")}
            }
        }
    }
}

class AttributeUpdater<T>(attribute: String, type: Class<T>, entityManager: EntityManager) {
    // instance data

    val entityId : ParameterExpression<Int>
    val attributeId : ParameterExpression<String>
    val value : ParameterExpression<T>
    val updateAttribute : Query

    // init

    init {
        val builder = entityManager.criteriaBuilder

        // delete attributes

        entityId    = builder.parameter(Int::class.java)
        attributeId = builder.parameter(String::class.java)
        value       = builder.parameter(type)

        val updateAttributeQuery = builder.createCriteriaUpdate(AttributeEntity::class.java)
        val from = updateAttributeQuery.from(AttributeEntity::class.java)

        updateAttributeQuery.set(attribute, value);

        updateAttributeQuery.where(builder.and(
            builder.equal(from.get<Int>("entity"), entityId),
            builder.equal(from.get<Int>("attribute"), attributeId)
        ))

        updateAttribute = entityManager.createQuery(updateAttributeQuery)
    }

    // public

    fun update(entity: Int, attribute: String, value: T) {
        updateAttribute
            .setParameter(entityId, entity)
            .setParameter(attributeId, attribute)
            .setParameter(this.value, value)
            .executeUpdate()
    }
}

class ObjectDeleter(entityManager: EntityManager) {
    // instance data

    val attributeId : ParameterExpression<Int>
    val entityId : ParameterExpression<Int>
    val deleteAttribute : Query
    val deleteEntity : Query

    // init

    init {
        val builder = entityManager.criteriaBuilder

        // delete attributes

        attributeId = builder.parameter<Int>(Int::class.java)

        val deleteAttributeQuery = builder.createCriteriaDelete(AttributeEntity::class.java)
        val from = deleteAttributeQuery.from(AttributeEntity::class.java)

        deleteAttributeQuery.where(builder.equal(from.get<Int>("entity"), attributeId))

        deleteAttribute = entityManager.createQuery(deleteAttributeQuery)

        // delete entity

        entityId = builder.parameter<Int>(Int::class.java)

        val criteriaQueryEntity = builder.createCriteriaDelete(EntityEntity::class.java)
        val fromEntity = criteriaQueryEntity.from(EntityEntity::class.java)
        criteriaQueryEntity.where(builder.equal(fromEntity.get<Int>("id"), entityId))

        deleteEntity = entityManager.createQuery(criteriaQueryEntity)
    }

    // public

    fun delete(obj: DataObject) {
        deleteAttribute.setParameter(attributeId, obj.getId()).executeUpdate()
        deleteEntity.setParameter(entityId, obj.getId()).executeUpdate()
    }
}


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

    fun update(obj: DataObject) {
        val builder: CriteriaBuilder = entityManager.criteriaBuilder

        // update attributes

        val properties = obj.type.properties
        for ( index in 1..<obj.values.size) {
            if ( obj.values[index] != obj.state!!.snapshot!![index]) {
                val property = properties[index]

                when ( property.type.baseType) {
                    String::class.java -> updater4("stringValue",  String::class.java).update(obj.getId(), property.name, obj.values[index] as String)
                    Integer::class.java -> updater4("intValue",  Integer::class.java).update(obj.getId(), property.name, obj.values[index] as Integer)
                    Int::class.java -> updater4("intValue",  Integer::class.java).update(obj.getId(), property.name, obj.values[index] as Integer)
                    // TODO REST
                    else -> {
                            throw Error("ouch")
                    }
                }
            }
        } // for

        // update entity

        val json = mapper.writeValueAsString(obj)

        val entityCriteriaQuery = builder.createCriteriaUpdate(EntityEntity::class.java)
        val entityFrom : Root<EntityEntity> = entityCriteriaQuery.from(EntityEntity::class.java)

        entityCriteriaQuery
            .set("json", json)
            .where(builder.equal(entityFrom.get<Int>("id"), obj.getId()))

        entityManager.createQuery(entityCriteriaQuery).executeUpdate()
    }

    fun delete(obj: DataObject) {
        if ( obj.getId() >= 0)
            deleter4(obj.type).delete(obj)
    }

    fun create(obj: DataObject) {
        val descriptor = obj.type

        val entity = EntityEntity(0, descriptor.name, mapper.writeValueAsString(obj))

        entityManager.persist(entity)

        obj.setId(entity.id)

        writer4(descriptor).write(obj, entityManager)
    }


    fun readFromEntity(state: TransactionState, objectDescriptor: ObjectDescriptor, entity: EntityEntity) : DataObject {
        // read json

        val node = mapper.readTree(entity.json)

        val obj = jsonReader4(objectDescriptor).read(node)

        obj.setId(entity.id) // TODO ?

        // set state

        state.register(ObjectState(obj, Status.MANAGED))

        // done

        return obj
    }

    fun read(state: TransactionState, objectDescriptor: ObjectDescriptor, attributes: List<AttributeEntity>, start: Int, end: Int) : DataObject {
        val values = arrayOfNulls<Any>(objectDescriptor.properties.size)

        values[0] = attributes[start].entity // id
        val obj = DataObject(objectDescriptor, null, values)

        val reader = reader4(objectDescriptor)

        for ( i in start..end) {
            val attribute = attributes[i]

            reader.read(obj, objectDescriptor.property(attribute.attribute).index, attribute)
        }

        // set state

        state.register(ObjectState(obj, Status.MANAGED))

        // done

        return obj
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

    private fun <T> updater4(attribute: String, type: Class<T>) : AttributeUpdater<T> {
        return AttributeUpdater(attribute, type, entityManager)//TODO updater.getOrPut(objectDescriptor.name) { -> ObjectUpdater(objectDescriptor) }
    }

    private fun deleter4(objectDescriptor: ObjectDescriptor) : ObjectDeleter {
        return ObjectDeleter(entityManager)//TODO deleter.getOrPut(objectDescriptor.name) { -> ObjectDeleter(entityManager) }
    }
}