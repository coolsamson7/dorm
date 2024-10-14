package org.sirius.dorm.model
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.ObjectManager
import org.sirius.dorm.`object`.DataObject
import org.sirius.dorm.`object`.MultiValuedRelation
import org.sirius.dorm.`object`.Property
import org.sirius.dorm.`object`.SingleValuedRelation
import org.sirius.dorm.persistence.entity.PropertyEntity
import org.sirius.dorm.transaction.Status

open class RelationDescriptor<T:Any>(name: String, val target: String, val multiplicity: Multiplicity, val cascadeDelete: Boolean, val removeOrphans: Boolean, val inverse: String?) : PropertyDescriptor<T>(name, false) {
    // instance data

    var owner : Boolean? = null
    var targetDescriptor: ObjectDescriptor? = null
    var inverseRelation : RelationDescriptor<*>? = null

    // override

    override fun createProperty(obj: DataObject, entity: PropertyEntity?) : Property {
        val relation =  if ( multiplicity.multiValued ) MultiValuedRelation(
            this,
            obj,
            entity,
            targetDescriptor!!
        )
        else SingleValuedRelation(this, obj, entity, targetDescriptor!!)

        return relation
    }

    override fun resolve(objectManager: ObjectManager, descriptor: ObjectDescriptor) {
        targetDescriptor = objectManager.getDescriptor(target)
        if ( inverse !== null) {
            inverseRelation = targetDescriptor!!.property(inverse) as RelationDescriptor<*>
            inverseRelation!!.inverseRelation = this // both directions
        }
    }

    override fun asRelation() : RelationDescriptor<T> {
        return this
    }

    override fun defaultValue(): Any? {
        return null // actually not called at all
    }

    override fun validate(value: Any?) {
        // currently not used
    }

    fun isOwner() : Boolean {
        return if ( owner == null ) computeOwner() else owner!!
    }

    // private

    protected fun fqn() : String {
        return  "${targetDescriptor?.name}.${name}"
    }

    private fun computeOwner() : Boolean {
        owner = fqn().compareTo(inverseRelation!!.fqn()) < 0
        return owner!!
    }

    // override Object
    override fun toString(): String {
        return fqn()
    }
}