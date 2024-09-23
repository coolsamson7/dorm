package org.sirius.dorm.persistence
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.PropertyDescriptor
import org.sirius.dorm.`object`.DataObject
import org.sirius.dorm.persistence.entity.PropertyEntity


typealias PropertyReader = (obj: DataObject, attribute: PropertyEntity) -> Unit

class ObjectReader(descriptor: ObjectDescriptor) {
    // instance data

    private val reader: Array<PropertyReader> =
        descriptor.properties
            .filter { property -> property.name !== "id" }
            .map { property -> reader4(property) }.toTypedArray()

    // public

    fun read(obj: DataObject, propertyDescriptor: PropertyDescriptor<Any>, attribute: PropertyEntity) {
        obj.values[propertyDescriptor.index].property = attribute

        reader[propertyDescriptor.index-1](obj, attribute)
    }

    // companion

    companion object {
        fun valueReader(clazz: Class<Any>) :  (attribute: PropertyEntity) -> Any {
            return when (clazz) {
                Boolean::class.javaObjectType -> { attribute: PropertyEntity -> attribute.intValue == 1 }

                Short::class.javaObjectType -> { attribute: PropertyEntity -> attribute.intValue.toShort() }

                Integer::class.javaObjectType -> { attribute: PropertyEntity -> attribute.intValue }

                Long::class.javaObjectType -> { attribute: PropertyEntity -> attribute.intValue.toLong() }

                Float::class.javaObjectType -> { attribute: PropertyEntity -> attribute.doubleValue.toFloat() }

                Double::class.javaObjectType -> { attribute: PropertyEntity -> attribute.doubleValue }

                String::class.javaObjectType -> { attribute: PropertyEntity -> attribute.stringValue }

                else -> throw Error("unsupported type")
            }
        }

        fun reader4(property: PropertyDescriptor<Any>): PropertyReader {
            if ( !property.isAttribute()) {
                return  { obj: DataObject, attribute: PropertyEntity ->
                    //obj.values[property.index].property = attribute
                }
            }
            else
                return when (property.asAttribute().baseType()) {
                    Boolean::class.javaObjectType -> { obj: DataObject, attribute: PropertyEntity ->
                        obj.values[property.index].init(property, attribute.intValue == 1)
                    }

                    String::class.javaObjectType -> { obj: DataObject, attribute: PropertyEntity ->
                        obj.values[property.index].init(property, attribute.stringValue)
                    }

                    Short::class.javaObjectType -> { obj: DataObject, attribute: PropertyEntity ->
                        obj.values[property.index].init(property, attribute.intValue.toShort())
                    }

                    Integer::class.javaObjectType -> { obj: DataObject, attribute: PropertyEntity ->
                        obj.values[property.index].init(property, attribute.intValue)
                    }

                    Long::class.javaObjectType -> { obj: DataObject, attribute: PropertyEntity ->
                        obj.values[property.index].init(property, attribute.intValue.toLong())
                    }

                    Float::class.javaObjectType -> { obj: DataObject, attribute: PropertyEntity ->
                        obj.values[property.index].init(property, attribute.doubleValue.toFloat())
                    }

                    Double::class.javaObjectType -> { obj: DataObject, attribute: PropertyEntity ->
                        obj.values[property.index].init(property, attribute.doubleValue)
                    }

                    else -> { _: DataObject, _: PropertyEntity ->
                        throw Error("unsupported type")
                    }
                }
        }
    }
}