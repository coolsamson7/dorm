package org.sirius.dorm.persistence
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.persistence.entity.PropertyEntity

typealias ReadProperty = (property: PropertyEntity) -> Any
typealias WriteProperty = (value: Any, property: PropertyEntity) -> Unit

class PropertyAccess {
    companion object {
        fun <T: Any> reader4(clazz: Class<T>) : (property: PropertyEntity) -> T {
            return when ( clazz ) {
                Boolean::class.javaObjectType -> { property: PropertyEntity ->
                    (property.intValue == 1) as T
                }

                String::class.javaObjectType -> { property: PropertyEntity ->
                    property.stringValue as T
                }

                Short::class.javaObjectType ->   { property: PropertyEntity ->
                    property.intValue.toShort() as T
                }

                Integer::class.javaObjectType ->   { property: PropertyEntity ->
                    property.intValue.toInt() as T
                }

                Long::class.javaObjectType ->   { property: PropertyEntity ->
                    property.intValue.toLong() as T
                }

                Float::class.javaObjectType ->   { property: PropertyEntity ->
                    property.doubleValue.toFloat() as T
                }

                Double::class.javaObjectType ->   { property: PropertyEntity ->
                    property.doubleValue as T
                }

                else -> {
                    throw Error("unsupported type")
                }
            } // when
        }

        fun <T: Any> writer4(clazz: Class<T>) : (value: T, property: PropertyEntity) -> Unit {
            return when ( clazz ) {
                Boolean::class.javaObjectType -> { value: T, property: PropertyEntity ->
                    property.intValue = if (value as Boolean) 1 else 0
                }

                String::class.javaObjectType -> { value: T, property: PropertyEntity ->
                    property.stringValue = value as String
                }

                Short::class.javaObjectType ->   { value: T, property: PropertyEntity ->
                    property.intValue = (value as Short).toInt()
                }

                Integer::class.javaObjectType ->   { value: T, property: PropertyEntity ->
                    property.intValue = (value as Integer).toInt()
                }

                Long::class.javaObjectType ->   { value: T, property: PropertyEntity ->
                    property.intValue = (value as Long).toInt()
                }

                Float::class.javaObjectType ->   { value: T, property: PropertyEntity ->
                    property.doubleValue = (value as Float).toDouble()
                }

                Double::class.javaObjectType ->   { value: T, property: PropertyEntity ->
                    property.doubleValue = value as Double
                }

                else -> {
                    throw Error("unsupported type")
                }
            }
        }
    }
}