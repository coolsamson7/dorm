package com.example.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import com.example.dorm.model.ObjectDescriptor
import com.example.dorm.model.PropertyDescriptor
import com.example.dorm.transaction.ObjectState

class DataObject(val type: ObjectDescriptor, var state : ObjectState?, val values: Array<Any?>) {
    // public

    fun property(property: String) : PropertyDescriptor<Any> {
        return type.property(property)
    }

    fun getId() : Int {
        return values[0] as Int
    }

    fun setId(id: Int) : Unit {
        values[0] = id
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