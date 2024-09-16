package com.example.dorm.query

import com.example.dorm.model.PropertyDescriptor
import com.example.dorm.persistence.entity.AttributeEntity
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Root

class PropertyPath(parent: ObjectPath, val property: PropertyDescriptor<Any>) : ObjectPath(parent) {
    override fun get(property: String) : ObjectPath {
        throw Error("ouch")
    }

    override fun path(root: Root<AttributeEntity>) : Path<Any> {
        return parent!!.path(root).get(property.name)
    }

    override fun attributeName() : String {
        return property.name
    }

    override fun <T> expression(root: Root<AttributeEntity>): Path<T> {
        return when ( property.type.baseType ) { // TODO: wrong place, right?
            String::class.java -> root.get<String>("stringValue") as Path<T>
            Short::class.java -> root.get<Int>("intValue") as Path<T>
            Integer::class.java -> root.get<Int>("intValue") as Path<T>
            Long::class.java -> root.get<Int>("intValue") as Path<T>
            Float::class.java -> root.get<Int>("doubleValue") as Path<T>
            Double::class.java -> root.get<Int>("doubleValue") as Path<T>

            else -> {
                throw Error("unsupported type")
            }
        }
    }

}