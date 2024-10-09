package org.sirius.dorm.`object`
/*
* @COPYRIGHT (C) 2023 Andreas Ernst
*
* All rights reserved
*/

import org.sirius.dorm.ObjectManager
import org.sirius.dorm.ObjectManagerError
import org.sirius.dorm.model.PropertyDescriptor
import org.sirius.dorm.persistence.entity.PropertyEntity

class Attribute(property: PropertyEntity?, var value: Any) : Property(property) { // TODO ? ??
    override fun <T:Any>  get(objectManager: ObjectManager) : T? {
        return value as T
    }

    override fun init(propertyDescriptor: PropertyDescriptor<Any>, value: Any?,  objectManager: ObjectManager) {
        this.value = value!!
    }

    override fun set(propertyDescriptor: PropertyDescriptor<Any>, value: Any?, objectManager: ObjectManager) : Boolean {
        propertyDescriptor.validate(value)

        return if ( value != this.value ) {
            this.value = value!!

            true
        }
        else false
    }

    override fun save(): Any {
        return value
    }

    override fun restore(state: Any) {
        // TODO
    }

    override fun isDirty(snapshot: Any) : Boolean {
        return value != snapshot
    }

    // override Object

    override fun toString(): String {
        return "${this.value}"
    }
}