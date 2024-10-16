package org.sirius.dorm.graphql
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.AttributeDescriptor
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.`object`.DataObject


class ObjectMutator(val objectManager: ObjectManager) {
    // bulk update

    fun bulkUpdate(results: List<DataObject>, input: Map<String,Any>) : Array<DataObject> {
        for ( obj in results)
            writeProperties(obj, input)

        return results.toTypedArray()
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
                return when ( targetType ) {
                    Short::class.javaObjectType -> argument.toShort()
                    Int::class.javaObjectType -> argument.toInt()
                    Long::class.javaObjectType -> argument.toLong()
                    Float::class.javaObjectType -> argument.toFloat()
                    Double::class.javaObjectType -> argument.toDouble()

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
            if (key == "id") // TODO ID
                continue

            val property = descriptor.property(key)

            if ( property.isAttribute())
                obj[key] = coerce(input[key]!!, property.asAttribute())

            else {
                if ( property.asRelation().multiplicity.multiValued) {
                    val array = input[key] as Collection<Map<String,Any>>

                    // recursion


                    val relation = obj.relation(key)

                    relation.clear()
                    for (element in array) {
                        val target = if (element.containsKey("id"))
                            objectManager.findById(descriptor, (element["id"] as Number).toLong())!!
                        else
                            objectManager.create(descriptor)

                        relation.add(writeProperties(target, element))
                    }
                }
                else {
                    val values = input[key] as Map<String,Any>

                    val target = if (values.containsKey("id"))
                        objectManager.findById(descriptor, (values["id"] as Number).toLong())!!
                    else
                        objectManager.create(descriptor)

                    writeProperties(target, values)

                    obj[key] = target
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