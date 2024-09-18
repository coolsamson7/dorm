package com.quasar.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import com.quasar.dorm.model.ObjectDescriptor
import com.quasar.dorm.model.PropertyDescriptor
import com.quasar.dorm.transaction.ObjectState

class DataObject(val type: ObjectDescriptor, var state : ObjectState?, val values: Array<Any?>) {
    // public

    fun property(property: String) : PropertyDescriptor<Any> {
        return type.property(property)
    }

    var id: Int
        get() = values[0] as Int
        set(value) { values[0] = value }

    // public operators

    fun <T:Any> value(index: Int, clazz: Class<T>) : T {
        return values[index] as T
    }

    operator fun get(name: String) : Any? {
        return values[property(name).index]
    }

    operator fun set(name: String, value: Any) {
        val property = property(name)

        if ( state != null)
            state!!.setValue(this, property, value)

        // set raw value

        values[property.index] = value
    }
}