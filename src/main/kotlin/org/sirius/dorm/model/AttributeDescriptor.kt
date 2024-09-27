package org.sirius.dorm.model
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.common.type.Type
import org.sirius.dorm.`object`.Attribute
import org.sirius.dorm.`object`.DataObject
import org.sirius.dorm.`object`.Property
import org.sirius.dorm.persistence.entity.PropertyEntity
import org.sirius.dorm.transaction.Status

class AttributeDescriptor<T:Any>(name: String, val type: Type<*>, val isPrimaryKey : Boolean = false) : PropertyDescriptor<T>(name) {
    // public

    fun baseType() : Class<*> {
        return type.baseType
    }

    // override

    override fun createProperty(obj: DataObject, status: Status, entity: PropertyEntity?) : Property {
        return Attribute(entity, defaultValue()!!)
    }

    override fun asAttribute() : AttributeDescriptor<T> {
        return this
    }

    override fun defaultValue() : Any? {
        return type.defaultValue()
    }

    override fun validate(value: Any?) {
        type.validate(value!!)
    }

    override fun isAttribute() : Boolean {
        return true
    }

    // override Object
    override fun toString(): String {
        return "${this.name}"
    }
}