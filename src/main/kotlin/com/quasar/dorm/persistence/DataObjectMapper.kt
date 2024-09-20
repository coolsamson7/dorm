package com.quasar.dorm.persistence
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.json.JSONReader
import com.quasar.dorm.model.ObjectDescriptor
import com.quasar.dorm.persistence.entity.AttributeEntity
import com.quasar.dorm.persistence.entity.EntityEntity
import com.quasar.dorm.transaction.TransactionState
import com.fasterxml.jackson.databind.ObjectMapper
import com.quasar.dorm.*
import com.quasar.dorm.model.PropertyDescriptor
import com.quasar.dorm.transaction.Operation
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.Query
import jakarta.persistence.criteria.ParameterExpression
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

typealias PropertyReader = (obj: DataObject, attribute: AttributeEntity) -> Unit
typealias PropertyWriter = (state: TransactionState, obj: DataObject, property: Int, attribute: AttributeEntity) -> Unit

class ObjectReader(descriptor: ObjectDescriptor) {
    // instance data

    private val reader: Array<PropertyReader> =
        descriptor.properties
            .filter { property -> property.name !== "id" }
            .map { property -> reader4(property) }.toTypedArray()

    // public

    fun read(obj: DataObject, propertyDescriptor: PropertyDescriptor<Any>, attribute: AttributeEntity) {
        reader[propertyDescriptor.index-1](obj, attribute)
    }

    // companion

    companion object {
        fun valueReader(clazz: Class<Any>) :  (attribute: AttributeEntity) -> Any {
            return when (clazz) {
                Boolean::class.javaObjectType -> { attribute: AttributeEntity -> attribute.intValue == 1 }

                Short::class.javaObjectType -> { attribute: AttributeEntity -> attribute.intValue.toShort() }

                Integer::class.javaObjectType -> { attribute: AttributeEntity -> attribute.intValue }

                Long::class.javaObjectType -> { attribute: AttributeEntity -> attribute.intValue.toLong() }

                Float::class.javaObjectType -> { attribute: AttributeEntity -> attribute.doubleValue.toFloat() }

                Double::class.javaObjectType -> { attribute: AttributeEntity -> attribute.doubleValue }

                String::class.javaObjectType -> { attribute: AttributeEntity -> attribute.stringValue }

                else -> throw Error("unsupported type")
            }
        }

        fun reader4(property: PropertyDescriptor<Any>): PropertyReader {
            if ( !property.isAttribute()) {
                return  { obj: DataObject, attribute: AttributeEntity ->
                    (obj.values[property.index] as Relation).property = attribute
                }
            }
            else
                return when (property.asAttribute().baseType()) {
                    Boolean::class.javaObjectType -> { obj: DataObject, attribute: AttributeEntity ->
                        obj.values[property.index].init(property, attribute.intValue == 1)
                    }

                    String::class.javaObjectType -> { obj: DataObject, attribute: AttributeEntity ->
                        obj.values[property.index].init(property, attribute.stringValue)
                    }

                    Short::class.javaObjectType -> { obj: DataObject, attribute: AttributeEntity ->
                        obj.values[property.index].init(property, attribute.intValue.toShort())
                    }

                    Integer::class.javaObjectType -> { obj: DataObject, attribute: AttributeEntity ->
                        obj.values[property.index].init(property, attribute.intValue)
                    }

                    Long::class.javaObjectType -> { obj: DataObject, attribute: AttributeEntity ->
                        obj.values[property.index].init(property, attribute.intValue.toLong())
                    }

                    Float::class.javaObjectType -> { obj: DataObject, attribute: AttributeEntity ->
                        obj.values[property.index].init(property, attribute.doubleValue.toFloat())
                    }

                    Double::class.javaObjectType -> { obj: DataObject, attribute: AttributeEntity ->
                        obj.values[property.index].init(property, attribute.doubleValue)
                    }

                    else -> { _: DataObject, _: AttributeEntity ->
                        throw Error("unsupported type")
                    }
                }
        }
    }
}

class ObjectWriter(private val descriptor: ObjectDescriptor) {
    // instance data

    private val writer: Array<PropertyWriter> = descriptor.properties
        .filter { property -> property.name !== "id" }
        .map { property -> writer4(property, descriptor.objectManager!!)}.toTypedArray()

    // public

    fun update(state: TransactionState, obj: DataObject, property: Int, attribute: AttributeEntity) {
        writer[property-1](state, obj, property, attribute)
    }

    fun write(state: TransactionState, obj: DataObject, entityManager: EntityManager) {
        var i = 1
        for ( writer in writer) {
            val propertyDescriptor = descriptor.properties[i]
            val attribute = AttributeEntity(obj.entity!!, propertyDescriptor.name, descriptor.name, "", 0, 0.0)

            // set entity, we may need it for flushing relations

            if (!propertyDescriptor.isAttribute())
                (obj.values[i] as Relation).property = attribute

            writer(state, obj, i++, attribute)

            entityManager.persist(attribute)
        }
    }

    // companion

    companion object {
        fun writer4(property: PropertyDescriptor<Any>, objectManager: ObjectManager) : PropertyWriter {
            if ( !property.isAttribute()) {
                return { state: TransactionState, obj: DataObject, index: Int, attribute: AttributeEntity ->
                    state.addOperation(AdjustRelation(obj.values[index] as Relation))
                }
            }
            else
                return when (property.asAttribute().baseType()) {
                    Boolean::class.javaObjectType -> { state: TransactionState, obj: DataObject, index: Int, attribute: AttributeEntity ->
                        attribute.intValue = if ( obj.values[index].get(objectManager) as Boolean) 1 else 0
                    }

                    String::class.javaObjectType -> { state: TransactionState, obj: DataObject, index: Int, attribute: AttributeEntity ->
                        attribute.stringValue = obj.values[index].get(objectManager) as String
                    }

                    Short::class.javaObjectType ->   { state: TransactionState, obj: DataObject, index: Int, attribute: AttributeEntity ->
                        attribute.intValue = (obj.values[index].get(objectManager) as Number).toInt()
                    }

                    Integer::class.javaObjectType ->   { state: TransactionState, obj: DataObject, index: Int, attribute: AttributeEntity ->
                        attribute.intValue = obj.values[index].get(objectManager) as Int
                    }

                    Long::class.javaObjectType ->   { state: TransactionState, obj: DataObject, index: Int, attribute: AttributeEntity ->
                        attribute.intValue = (obj.values[index].get(objectManager) as Number).toInt()
                    }

                    Float::class.javaObjectType ->   { state: TransactionState, obj: DataObject, index: Int, attribute: AttributeEntity ->
                        attribute.doubleValue = (obj.values[index].get(objectManager) as Number).toDouble()
                    }

                    Double::class.javaObjectType ->   { state: TransactionState, obj: DataObject, index: Int, attribute: AttributeEntity ->
                        attribute.doubleValue = obj.values[index].get(objectManager) as Double
                    }

                    else -> { _: TransactionState, _: DataObject, _: Int, _: AttributeEntity ->
                        throw Error("unsupported type")
                    }
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
            builder.equal(from.get<EntityEntity>("entity").get<Int>("id"), entityId),
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

        entityId = builder.parameter(Int::class.java)

        val criteriaQueryEntity = builder.createCriteriaDelete(EntityEntity::class.java)
        val fromEntity = criteriaQueryEntity.from(EntityEntity::class.java)
        criteriaQueryEntity.where(builder.equal(fromEntity.get<Int>("id"), entityId))

        deleteEntity = entityManager.createQuery(criteriaQueryEntity)
    }

    // public

    fun delete(obj: DataObject) {
        deleteAttribute.setParameter(attributeId, obj.id).executeUpdate()
        deleteEntity.setParameter(entityId, obj.id).executeUpdate()
    }
}

class AdjustRelation(val relation: Relation) : Operation() {
    override fun execute() {
        relation.flush()
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

    fun update(state: TransactionState, obj: DataObject) {
        val builder = entityManager.criteriaBuilder

        val objectManager = state.objectManager

        // update attributes

        val properties = obj.type.properties
        for ( index in 1..<obj.values.size) {
            if ( obj.values[index].isDirty(obj.state!!.snapshot!![index])) {
                val property = properties[index]

                if ( property.isAttribute())
                    when ( property.asAttribute().type.baseType) {
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
                                throw Error("ouch")
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

        entityCriteriaQuery
            .set("json", json)
            .where(builder.equal(entityFrom.get<Int>("id"), obj.id))

        entityManager.createQuery(entityCriteriaQuery).executeUpdate()
    }

    fun delete(state: TransactionState, obj: DataObject) {
        entityManager.remove(obj.entity!!)
    }

    fun create(state: TransactionState, obj: DataObject) {
        val descriptor = obj.type

        obj.entity = EntityEntity(0, descriptor.name, mapper.writeValueAsString(obj))

        entityManager.persist(obj.entity) // we need the id...is that required, think of a lifeccle hook?

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

    fun read(state: TransactionState, objectDescriptor: ObjectDescriptor, attributes: List<AttributeEntity>, start: Int, end: Int) : DataObject {
        return state.retrieve(attributes[start].entity.id) {
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