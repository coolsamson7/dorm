package com.quasar.dorm.model
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.type.Type

class PropertyDescriptor<T:Any>(val name: String, val type: Type<T>, val isPrimaryKey : Boolean = false) {
    var index = 0

    fun defaultValue() : Any {
        return type.defaultValue()
    }

    fun validate(value: Any) {
        type.validate(value)
    }
}