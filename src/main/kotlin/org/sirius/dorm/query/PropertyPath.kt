package org.sirius.dorm.query
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import jakarta.persistence.criteria.AbstractQuery
import jakarta.persistence.criteria.CriteriaBuilder
import org.sirius.dorm.model.PropertyDescriptor
import org.sirius.dorm.persistence.entity.PropertyEntity
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Root

class PropertyPath(parent: ObjectPath, val property: PropertyDescriptor<Any>) : ObjectPath(parent) {
    override fun get(property: String) : ObjectPath {
        throw Error("ouch")
    }

    override fun attributeName() : String {
        return property.name
    }

    override fun type() : Class<out Any> {
        return property.asAttribute().type.baseType
    }

    override fun <T> expression(root: Root<PropertyEntity>, builder: CriteriaBuilder, query: AbstractQuery<*>): Path<T> {
        val parentPath = this.parent?.expression<T>(root, builder, query)!!

        return when ( property.name ) {
            "id"-> parentPath.get<String>("entity").get<Int>("id") as Path<T>
            "versionCounter"-> parentPath.get<String>("entity").get<Long>("versionCounter") as Path<T>
            // TODO status?
            else -> {
                when ( property.asAttribute().type.baseType ) {
                    String::class.javaObjectType -> parentPath.get<String>("stringValue") as Path<T>
                    Short::class.javaObjectType -> parentPath.get<Int>("intValue") as Path<T>
                    Int::class.javaObjectType -> parentPath.get<Int>("intValue") as Path<T>
                    Integer::class.javaObjectType -> parentPath.get<Int>("intValue") as Path<T>
                    Long::class.javaObjectType -> parentPath.get<Int>("intValue") as Path<T>
                    Float::class.javaObjectType -> parentPath.get<Int>("doubleValue") as Path<T>
                    Double::class.javaObjectType -> parentPath.get<Int>("doubleValue") as Path<T>

                    else -> {
                        throw Error("unsupported type ${property.asAttribute().type.baseType}")
                    }
                }
            }
        }
    }
}