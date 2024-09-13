package com.example.dorm.model

import com.example.dorm.type.Type

class PropertyDescriptor<T:Any>(val name: String, val type: Type<T>) {
    var index = 0

    fun defaultValue() : Any {
        return type.defaultValue()
    }

    fun validate(value: Any) {
        type.validate(value)
    }
}