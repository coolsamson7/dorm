package com.example.dorm

import com.example.dorm.model.ObjectDescriptor
import com.example.dorm.model.PropertyDescriptor
import com.example.dorm.transaction.ObjectState

class DataObject(val type: ObjectDescriptor, var id: Int, var state : ObjectState?, val values: Array<Any?>) {
    // public

    public fun property(property: String) : PropertyDescriptor<Any> {
        return type.property(property)
    }

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