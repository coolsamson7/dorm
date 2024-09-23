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
import org.sirius.dorm.persistence.entity.AttributeEntity
import org.sirius.dorm.transaction.TransactionState

class ObjectUpdater(private val descriptor: ObjectDescriptor) {
    // instance data

    private val writer: Array<PropertyWriter> = descriptor.properties
        //.filter { property -> property.name !== "id" }
        .map { property -> writer4(property, descriptor.objectManager!!)}.toTypedArray()

    // public

    fun update(state: TransactionState, obj: DataObject) {
        var i = 0
        val snapshot = obj.state!!.snapshot!!
        for ( writer in writer) {
            val property = obj.values[i]

            if ( property.isDirty(snapshot[i]))
                writer(state, obj, i, property.property!!)

            i++
        }
    }

    // companion

    companion object {
        fun writer4(property: PropertyDescriptor<Any>, objectManager: ObjectManager) : PropertyWriter {
            if ( !property.isAttribute()) {
                return { state: TransactionState, obj: DataObject, index: Int, attribute: AttributeEntity ->
                    (obj.values[index] as Relation).flush()
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