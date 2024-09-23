package org.sirius.dorm.query
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.model.PropertyDescriptor
import org.sirius.dorm.persistence.entity.PropertyEntity
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Root

class PropertyPath(parent: ObjectPath, val property: PropertyDescriptor<Any>) : ObjectPath(parent) {
    override fun get(property: String) : ObjectPath {
        throw Error("ouch")
    }

    override fun path(root: Root<PropertyEntity>) : Path<Any> {
        return parent!!.path(root).get(property.name)
    }

    override fun attributeName() : String {
        return property.name
    }

    override fun type() : Class<Any> {
        return property.asAttribute().type.baseType
    }

    override fun <T> expression(root: Root<PropertyEntity>): Path<T> {
        return if ( property.name == "id")
            root.get<String>("entity").get<Int>("id") as Path<T>

        else when ( property.asAttribute().type.baseType ) {
            String::class.javaObjectType -> root.get<String>("stringValue") as Path<T>
            Short::class.javaObjectType -> root.get<Int>("intValue") as Path<T>
            Int::class.javaObjectType -> root.get<Int>("intValue") as Path<T>
            Integer::class.javaObjectType -> root.get<Int>("intValue") as Path<T>
            Long::class.javaObjectType -> root.get<Int>("intValue") as Path<T>
            Float::class.javaObjectType -> root.get<Int>("doubleValue") as Path<T>
            Double::class.javaObjectType -> root.get<Int>("doubleValue") as Path<T>

            else -> {
                throw Error("unsupported type")
            }
        }
    }
}