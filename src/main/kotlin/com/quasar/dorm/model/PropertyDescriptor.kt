package com.quasar.dorm.model
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.*
import com.quasar.dorm.persistence.entity.AttributeEntity
import com.quasar.dorm.type.Type

abstract class PropertyDescriptor<T:Any>(val name: String) {
    var index = 0

    abstract fun createProperty(entity: AttributeEntity?) : Property

    abstract fun defaultValue() : Any?

    abstract fun validate(value: Any?)

    open fun isAttribute() : Boolean {
        return false
    }

    open fun asAttribute() :AttributeDescriptor<T> {
        throw Error("${name} is a relation")
    }

    open fun asRelation() :RelationDescriptor<T> {
        throw Error("${name} is a relation")
    }

    open fun resolve(objectManager: ObjectManager, descriptor: ObjectDescriptor) {
    }
}

class AttributeDescriptor<T:Any>(name: String, val type: Type<T>, val isPrimaryKey : Boolean = false) : PropertyDescriptor<T>(name) {
    // public

    fun baseType() : Class<*> {
        return type.baseType
    }

    // override

    override fun createProperty(entity: AttributeEntity?) : Property {
        return Attribute(entity, defaultValue()!!)
    }

    override fun asAttribute() :AttributeDescriptor<T> {
        return this
    }

    override fun defaultValue() : Any? {
        return type.defaultValue()
    }

    override fun validate(value: Any?) {
        type.validate(value!!)
    }

    override fun isAttribute() : Boolean {
        return true
    }
}


enum class Multiplicity(val optional: Boolean, val mutliValued: Boolean) {
    ZERO_OR_ONE(true, false),
    ONE(false, false),
    MANY(false, true),
    ZERO_OR_MANY(true, true)
}
open class RelationDescriptor<T:Any>(name: String, val target: String, val multiplicity: Multiplicity) : PropertyDescriptor<T>(name) {
    // instance data

    var targetDescriptor: ObjectDescriptor? = null

    // override

    override fun createProperty(entity: AttributeEntity?) : Property {
        return if ( multiplicity === Multiplicity.ONE ) SingleValuedRelation(entity, targetDescriptor!!) else MultiValuedRelation(entity, targetDescriptor!!)
    }

    override fun resolve(objectManager: ObjectManager, descriptor: ObjectDescriptor) {
        targetDescriptor = objectManager.getDescriptor(target)
    }

    override fun asRelation() :RelationDescriptor<T> {
        return this
    }

    override fun defaultValue(): Any? {
        TODO("Not yet implemented")
    }

    override fun validate(value: Any?) {
        TODO("Not yet implemented")
    }
}