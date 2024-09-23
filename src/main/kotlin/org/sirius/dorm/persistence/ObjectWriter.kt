package org.sirius.dorm.persistence
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.PropertyDescriptor
import org.sirius.dorm.`object`.DataObject
import org.sirius.dorm.`object`.Relation
import org.sirius.dorm.persistence.entity.PropertyEntity
import org.sirius.dorm.transaction.TransactionState
import jakarta.persistence.EntityManager

typealias PropertyWriter = (state: TransactionState, obj: DataObject, property: Int, attribute: PropertyEntity) -> Unit

class ObjectWriter(private val descriptor: ObjectDescriptor) {
    // instance data

    private val writer: Array<PropertyWriter> = descriptor.properties
        .filter { property -> property.name !== "id" }
        .map { property -> writer4(property, descriptor.objectManager!!)}.toTypedArray()

    // public

    fun update(state: TransactionState, obj: DataObject, property: Int, attribute: PropertyEntity) {
        writer[property-1](state, obj, property, attribute)
    }

    fun write(state: TransactionState, obj: DataObject, entityManager: EntityManager) {
        var i = 1
        for ( writer in writer) {
            val propertyDescriptor = descriptor.properties[i]
            val attribute = PropertyEntity(obj.entity!!, propertyDescriptor.name, descriptor.name, "", 0, 0.0)

            // set entity, we may need it for flushing relations

            obj.values[i].property = attribute

            writer(state, obj, i++, attribute)

            entityManager.persist(attribute)
        }
    }

    // companion

    companion object {
        fun writer4(property: PropertyDescriptor<Any>, objectManager: ObjectManager) : PropertyWriter {
            if ( !property.isAttribute()) {
                return { state: TransactionState, obj: DataObject, index: Int, attribute: PropertyEntity ->
                    state.addOperation(AdjustRelation(obj.values[index] as Relation))
                }
            }
            else
                return when (property.asAttribute().baseType()) {
                    Boolean::class.javaObjectType -> { state: TransactionState, obj: DataObject, index: Int, attribute: PropertyEntity ->
                        attribute.intValue = if ( obj.values[index].get(objectManager) as Boolean) 1 else 0
                    }

                    String::class.javaObjectType -> { state: TransactionState, obj: DataObject, index: Int, attribute: PropertyEntity ->
                        attribute.stringValue = obj.values[index].get(objectManager) as String
                    }

                    Short::class.javaObjectType ->   { state: TransactionState, obj: DataObject, index: Int, attribute: PropertyEntity ->
                        attribute.intValue = (obj.values[index].get(objectManager) as Number).toInt()
                    }

                    Integer::class.javaObjectType ->   { state: TransactionState, obj: DataObject, index: Int, attribute: PropertyEntity ->
                        attribute.intValue = obj.values[index].get(objectManager) as Int
                    }

                    Long::class.javaObjectType ->   { state: TransactionState, obj: DataObject, index: Int, attribute: PropertyEntity ->
                        attribute.intValue = (obj.values[index].get(objectManager) as Number).toInt()
                    }

                    Float::class.javaObjectType ->   { state: TransactionState, obj: DataObject, index: Int, attribute: PropertyEntity ->
                        attribute.doubleValue = (obj.values[index].get(objectManager) as Number).toDouble()
                    }

                    Double::class.javaObjectType ->   { state: TransactionState, obj: DataObject, index: Int, attribute: PropertyEntity ->
                        attribute.doubleValue = obj.values[index].get(objectManager) as Double
                    }

                    else -> { _: TransactionState, _: DataObject, _: Int, _: PropertyEntity ->
                        throw Error("unsupported type")
                    }
                }
        }
    }
}