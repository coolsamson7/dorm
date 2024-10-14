package org.sirius.dorm.model
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.`object`.DataObject
import org.sirius.dorm.ObjectManager
import org.sirius.dorm.`object`.Property
import org.sirius.dorm.transaction.Status

class ObjectDescriptor(val name: String, val properties: Array<PropertyDescriptor<Any>>, var objectManager: ObjectManager? = null) {
    // instance data

    init {
        var index = 0
        for ( property in properties)
            property.index = index++
    }

    // public

    fun createValues(obj: DataObject): Array<Property> {
        return properties.map { property -> property.createProperty(obj, null) }.toTypedArray()
    }

    fun resolve(objectManager: ObjectManager) {
        if ( this.objectManager == null) {
            this.objectManager = objectManager
            for (property in properties)
                property.resolve(objectManager, this)
        }
    }

    fun create(vararg status: Status) : DataObject {
        return DataObject(this, *status)
    }

    fun property(property: String) : PropertyDescriptor<Any> {
        return properties.find { prop -> prop.name == property}!!
    }

    // override Object
    override fun toString(): String {
        return "${this.name}"
    }
}