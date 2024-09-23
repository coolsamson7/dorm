package org.sirius.dorm.`object`
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.RelationDescriptor
import org.sirius.dorm.persistence.entity.PropertyEntity

abstract class Relation(val relation: RelationDescriptor<*>, val targetDescriptor: ObjectDescriptor, property: PropertyEntity?) : Property(property) {
    abstract fun isLoaded(): Boolean

    override fun save(): Any {
        return this
    }

    override fun restore(state: Any) {
    }

    override fun isDirty(snapshot: Any) : Boolean {
        return isLoaded()
    }

    protected fun inverseRelation(obj: DataObject?) : Relation? {
        if ( obj !== null && isLoaded()) {
            val relation = obj.values[relation.inverseRelation!!.index] as Relation
            return if (relation.isLoaded()) relation else null
        }
        else return null
    }

    fun relations() : MutableSet<PropertyEntity> {
        return if ( relation.isOwner()) property!!.targets else property!!.sources
    }

    // abstract

    abstract fun addInverse(element: DataObject)
    abstract fun removeInverse(element: DataObject)
}