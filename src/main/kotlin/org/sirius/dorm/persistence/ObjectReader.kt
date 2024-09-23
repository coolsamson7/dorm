package org.sirius.dorm.persistence
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.PropertyDescriptor
import org.sirius.dorm.`object`.DataObject
import org.sirius.dorm.`object`.Relation
import org.sirius.dorm.persistence.entity.AttributeEntity


typealias PropertyReader = (obj: DataObject, attribute: AttributeEntity) -> Unit

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