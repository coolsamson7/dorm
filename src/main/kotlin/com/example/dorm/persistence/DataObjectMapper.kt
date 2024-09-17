package com.example.dorm.persistence

import com.example.dorm.DataObject
import com.example.dorm.ObjectManager
import com.example.dorm.json.JSONReader
import com.example.dorm.json.ObjectModule
import com.example.dorm.model.ObjectDescriptor
import com.example.dorm.persistence.entity.AttributeEntity
import com.example.dorm.persistence.entity.EntityEntity
import com.example.dorm.transaction.ObjectState
import com.example.dorm.transaction.Status
import com.example.dorm.transaction.TransactionState
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.CriteriaBuilder
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
                    attribute.intValue = (obj.values[property] as Short).toInt()
                }

                Integer::class.java ->   { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    attribute.intValue = obj.values[property] as Int
                }

                Long::class.java ->   { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    attribute.intValue = (obj.values[property] as Long).toInt()
                }

                Float::class.java ->   { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    attribute.doubleValue = (obj.values[property] as Float).toDouble()
                }

                Double::class.java ->   { obj: DataObject, property: Int, attribute: AttributeEntity ->
                    attribute.doubleValue = obj.values[property] as Double
                }

                else -> { _: DataObject, _: Int, _: AttributeEntity -> throw Error("unsupported type")}
            }
        }
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

    // public

    fun update(obj: DataObject) {
        val builder: CriteriaBuilder = entityManager.criteriaBuilder

        // update attributes

        val criteriaQuery = builder.createQuery(AttributeEntity::class.java)
        val attributeEntity = criteriaQuery.from(AttributeEntity::class.java)

        criteriaQuery
            .select(attributeEntity)
            .where(builder.equal(attributeEntity.get<Int>("entity"), obj.getId()))

        val query = entityManager.createQuery(criteriaQuery)
        val attributes = query.resultList

        val writer = writer4(obj.type)

        for ( attribute in attributes) {
            val property = obj.property(attribute.attribute)
            val index = property.index

            if ( obj.values[index] != obj.state!!.snapshot!![index])
                writer.update(obj, index, attribute)
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
        if ( obj.getId() < 0)
            return

        val builder = entityManager.criteriaBuilder

        // delete attributes

        val criteriaQuery = builder.createCriteriaDelete(AttributeEntity::class.java)
        val from = criteriaQuery.from(AttributeEntity::class.java)

        criteriaQuery.where(builder.equal(from.get<Int>("entity"), obj.getId()))

        entityManager.createQuery(criteriaQuery).executeUpdate()

        // delete entity

        val criteriaQueryEntity = builder.createCriteriaDelete(EntityEntity::class.java)
        val fromEntity = criteriaQueryEntity.from(EntityEntity::class.java)

        criteriaQueryEntity.where(builder.equal(fromEntity.get<Int>("id"), obj.getId()))

        entityManager.createQuery(criteriaQueryEntity).executeUpdate()
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

        //TODO IDobj.id = entity.id

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
}