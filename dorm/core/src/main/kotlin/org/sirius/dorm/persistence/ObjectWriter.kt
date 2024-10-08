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

open class ObjectWriter(protected val descriptor: ObjectDescriptor) {
    // instance data

    protected val writer: Array<PropertyWriter> = descriptor.properties
        .map { property -> writer4(property, descriptor.objectManager!!)}.toTypedArray()

    companion object {
        fun writer4(property: PropertyDescriptor<Any>, objectManager: ObjectManager) : PropertyWriter {
            if ( !property.isAttribute()) {
                return { state: TransactionState, obj: DataObject, index: Int, attribute: PropertyEntity ->
                    //(obj.values[index] as Relation).flush()
                    state.addOperation(AdjustRelation(obj.values[index] as Relation))
                }
            }
            else if (property.readOnly)
                return { state: TransactionState, obj: DataObject, index: Int, attribute: PropertyEntity ->
                    // noop
                }
            else
                return when (property.asAttribute().baseType()) {
                    Boolean::class.javaObjectType -> { state: TransactionState, obj: DataObject, index: Int, attribute: PropertyEntity ->
                        attribute.intValue = if ( obj.values[index].get<Boolean>(objectManager)!!) 1 else 0
                    }

                    String::class.javaObjectType -> { state: TransactionState, obj: DataObject, index: Int, attribute: PropertyEntity ->
                        attribute.stringValue = obj.values[index].get<String>(objectManager)!!
                    }

                    Short::class.javaObjectType ->   { state: TransactionState, obj: DataObject, index: Int, attribute: PropertyEntity ->
                        attribute.intValue = obj.values[index].get<Number>(objectManager)!!.toInt()
                    }

                    Integer::class.javaObjectType ->   { state: TransactionState, obj: DataObject, index: Int, attribute: PropertyEntity ->
                        attribute.intValue = obj.values[index].get<Int>(objectManager)!!
                    }

                    Long::class.javaObjectType ->   { state: TransactionState, obj: DataObject, index: Int, attribute: PropertyEntity ->
                        attribute.intValue = obj.values[index].get<Number>(objectManager)!!.toInt()
                    }

                    Float::class.javaObjectType ->   { state: TransactionState, obj: DataObject, index: Int, attribute: PropertyEntity ->
                        attribute.doubleValue = obj.values[index].get<Number>(objectManager)!!.toDouble()
                    }

                    Double::class.javaObjectType ->   { state: TransactionState, obj: DataObject, index: Int, attribute: PropertyEntity ->
                        attribute.doubleValue = obj.values[index].get<Double>(objectManager)!!
                    }

                    else -> { _: TransactionState, _: DataObject, _: Int, _: PropertyEntity ->
                        throw Error("unsupported type")
                    }
                }
        }
    }
}