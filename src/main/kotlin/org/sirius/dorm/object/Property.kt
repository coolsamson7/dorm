package org.sirius.dorm.`object`
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */


import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.PropertyDescriptor
import org.sirius.dorm.persistence.entity.AttributeEntity

abstract class Property(var property: AttributeEntity?) {
    abstract fun get(objectManager: ObjectManager) : Any?

    abstract fun set(propertyDescriptor: PropertyDescriptor<Any>, value: Any?) : Boolean

    open fun init(propertyDescriptor: PropertyDescriptor<Any>, value: Any?) {
        this.set(propertyDescriptor, value)
    }

    // snapshot stuff

    abstract fun save(): Any;

    abstract fun restore(state: Any);

    open fun isDirty(snapshot: Any) : Boolean {
        return false
    }

    open fun flush() {}
}