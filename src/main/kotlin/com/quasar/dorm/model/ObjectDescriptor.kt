package com.quasar.dorm.model
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.DataObject
import com.quasar.dorm.ObjectManager
import com.quasar.dorm.Property

class ObjectDescriptor(val name: String, val properties: Array<PropertyDescriptor<Any>>, var objectManager: ObjectManager? = null) {
    // instance data

    init {
        var index = 0
        for ( property in properties)
            property.index = index++
    }

    // public

    private fun createValues(): Array<Property> {
        return properties.map { property -> property.createProperty(null) }.toTypedArray()
    }

    fun resolve(objectManager: ObjectManager) {
        this.objectManager = objectManager
        for ( property in properties)
            property.resolve(objectManager, this)
    }

    fun create() : DataObject {
        return DataObject(this, null, createValues())
    }

    fun property(property: String) : PropertyDescriptor<Any> {
        return properties.find { prop -> prop.name == property}!!
    }
}