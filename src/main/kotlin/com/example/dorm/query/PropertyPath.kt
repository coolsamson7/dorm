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

    override fun expression(root: Root<AttributeEntity>): Path<Any> {
        return when ( property.type.baseType ) { // TODO: wrong place, right?
            String::class.java -> root.get<String>("stringValue") as Path<Any>
            Short::class.java -> root.get<Int>("intValue") as Path<Any>
            Integer::class.java -> root.get<Int>("intValue") as Path<Any>
            Long::class.java -> root.get<Int>("intValue") as Path<Any>
            Float::class.java -> root.get<Int>("doubleValue") as Path<Any>
            Double::class.java -> root.get<Int>("doubleValue") as Path<Any>

            else -> {
                throw Error("unsupported type")
            }
        }
    }

}