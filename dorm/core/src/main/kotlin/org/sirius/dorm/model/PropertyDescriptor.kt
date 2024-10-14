package org.sirius.dorm.model
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.*
import org.sirius.dorm.persistence.entity.PropertyEntity
import org.sirius.dorm.`object`.*
import org.sirius.dorm.transaction.Status

abstract class PropertyDescriptor<T:Any>(val name: String, val readOnly: Boolean) {
    var index = 0

    abstract fun createProperty(obj: DataObject, entity: PropertyEntity?) : Property

    abstract fun defaultValue() : Any?

    abstract fun validate(value: Any?)

    open fun isAttribute() : Boolean {
        return false
    }

    open fun asAttribute() :AttributeDescriptor<T> {
        throw Error("${name} is a relation")
    }

    open fun asRelation() :RelationDescriptor<T> {
        throw Error("${name} is a relation")
    }

    open fun resolve(objectManager: ObjectManager, descriptor: ObjectDescriptor) {
    }
}


