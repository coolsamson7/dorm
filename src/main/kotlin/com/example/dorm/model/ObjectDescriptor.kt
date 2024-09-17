package com.example.dorm.model
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.example.dorm.DataObject

class ObjectDescriptor(val name: String, val properties: Array<PropertyDescriptor<Any>>) {
    // instance data

    init {
        var index = 0
        for ( property in properties)
            property.index = index++
    }

    // public

    fun create() : DataObject {
        val values : Array<Any?> = properties.map { property -> property.defaultValue() }.toTypedArray()

        return DataObject(this, null, values)
    }

    fun property(property: String) : PropertyDescriptor<Any> {
        return properties.find { prop -> prop.name == property}!!
    }
}