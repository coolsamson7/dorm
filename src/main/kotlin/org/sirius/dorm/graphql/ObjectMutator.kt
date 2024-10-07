package org.sirius.dorm.graphql
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.AttributeDescriptor
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.PropertyDescriptor
import org.sirius.dorm.`object`.DataObject


class ObjectMutator(val objectManager: ObjectManager) {
    // update

    fun update(descriptor: ObjectDescriptor, input: Map<String,Any>) : DataObject {
        val obj = objectManager.findById(descriptor, (input.get("id") as Number).toLong())!!

        // iterate over properties

        for ( key in input.keys) {
            if (key == "id")
                continue

            val property = descriptor.property(key)

            if ( property.isAttribute())
                obj[key] = coerce(input[key]!!, property.asAttribute())
        }

        return obj
    }

    private fun coerce(value: Any, property: AttributeDescriptor<*>): Any {
        if ( !property.type.baseType.isAssignableFrom( value.javaClass)) {
            println("?")
        }

        return value
    }

    // create

    fun create(descriptor: ObjectDescriptor, input: Map<String,Any>) : DataObject {
        val newObject = objectManager.create(descriptor)

        // iterate over properties

        for ( key in input.keys) {
            if (key == "id")
                continue

            val property = descriptor.property(key)

            if ( property.isAttribute())
                newObject[key] = coerce(input[key]!!, property.asAttribute())
        }

        return newObject
    }
}