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
import kotlin.reflect.full.isSuperclassOf


class ObjectMutator(val objectManager: ObjectManager) {
    // bulk update

    fun bulkUpdate(descriptor: ObjectDescriptor,  results: List<DataObject>, input: Map<String,Any>) : Int {
        for ( obj in results)
            writeProperties(obj, input)

        return results.size
    }

    // update

    fun update(descriptor: ObjectDescriptor, input: Map<String,Any>) : DataObject {
        return writeProperties(objectManager.findById(descriptor, (input.get("id") as Number).toLong())!!, input)
    }

    private fun coerce(value: Any, property: AttributeDescriptor<*>): Any {
        val targetType = property.type.baseType
        if ( !targetType.isAssignableFrom( value.javaClass)) {
            val argument = value as Number
            if (Number::class.java.isAssignableFrom(targetType)) {
                when ( targetType ) {
                    Short::class.java -> argument.toShort()
                    Int::class.java -> argument.toInt()
                    Long::class.java -> argument.toLong()
                    Float::class.java -> argument.toFloat()
                    Double::class.java -> argument.toDouble()

                    else -> {
                        throw Error("no coercion possible")
                    }
                }
            }
            else throw Error("no coercion possible")
        }

        return value
    }

    private fun writeProperties(obj: DataObject, input: Map<String,Any>) : DataObject {
        val descriptor = obj.type

        for ( key in input.keys) {
            if (key == "id")
                continue

            val property = descriptor.property(key)

            if ( property.isAttribute())
                obj[key] = coerce(input[key]!!, property.asAttribute())

            else {
                if ( property.asRelation().multiplicity.mutliValued) {
                    val array = input[key] as Collection<Map<String,Any>>

                    // recursion


                    val relation = obj.relation(key)

                    relation.clear()
                    for (obj in array) {
                        val target = if (obj.containsKey("id"))
                            objectManager.findById(descriptor, (obj["id"] as Number).toLong())!!
                        else
                            objectManager.create(descriptor)

                        relation.add(writeProperties(target, obj))
                    }
                }
                else {
                    val values = input[key] as Map<String,Any>

                    val target = if (values.containsKey("id"))
                        objectManager.findById(descriptor, (values["id"] as Number).toLong())!!
                    else
                        objectManager.create(descriptor)

                    writeProperties(target, values)
                }
            }
        }

        return obj
    }

    // create

    fun create(descriptor: ObjectDescriptor, input: Map<String,Any>) : DataObject {
        return writeProperties(objectManager.create(descriptor), input)
    }
}