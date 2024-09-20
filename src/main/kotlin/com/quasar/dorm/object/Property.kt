package com.quasar.dorm.`object`

import com.quasar.dorm.ObjectManager
import com.quasar.dorm.model.PropertyDescriptor

abstract class Property() {
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