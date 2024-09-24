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

open class RelationDescriptor<T:Any>(name: String, val target: String, val multiplicity: Multiplicity, val cascade: Cascade?, val inverse: String?) : PropertyDescriptor<T>(name) {
    // instance data

    var targetDescriptor: ObjectDescriptor? = null
    var inverseRelation : RelationDescriptor<*>? = null

    // override

    override fun createProperty(obj: DataObject, status: Status, entity: PropertyEntity?) : Property {
        val relation =  if ( multiplicity.mutliValued ) MultiValuedRelation(
            this,
            status,
            obj,
            entity,
            targetDescriptor!!
        )
        else SingleValuedRelation(this, status, obj, entity, targetDescriptor!!)

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
        return inverse !== null
    }
}