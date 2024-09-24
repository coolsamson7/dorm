package org.sirius.dorm.model
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.*
import org.sirius.dorm.persistence.entity.PropertyEntity
import org.sirius.common.type.Type
import org.sirius.dorm.`object`.*
import org.sirius.dorm.transaction.Status

abstract class PropertyDescriptor<T:Any>(val name: String) {
    var index = 0

    abstract fun createProperty(obj: DataObject, status: Status, entity: PropertyEntity?) : Property

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

    override fun createProperty(obj: DataObject, status: Status, entity: PropertyEntity?) : Property {
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
open class RelationDescriptor<T:Any>(name: String, val target: String, val multiplicity: Multiplicity, val inverse: String?) : PropertyDescriptor<T>(name) {
    // instance data

    var targetDescriptor: ObjectDescriptor? = null
    var inverseRelation : RelationDescriptor<*>? = null

    // override

    override fun createProperty(obj: DataObject, status: Status, entity: PropertyEntity?) : Property {
        val relation =  if ( multiplicity.mutliValued ) MultiValuedRelation(this, status, obj, entity, targetDescriptor!!) else SingleValuedRelation(this, status, obj, entity, targetDescriptor!!)

        return relation
    }

    override fun resolve(objectManager: ObjectManager, descriptor: ObjectDescriptor) {
        targetDescriptor = objectManager.getDescriptor(target)
        if ( inverse !== null) {
            inverseRelation = targetDescriptor!!.property(inverse) as RelationDescriptor<*>
            inverseRelation!!.inverseRelation = this // both directions
        }
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

    fun isOwner() : Boolean {
        return inverse !== null
    }
}