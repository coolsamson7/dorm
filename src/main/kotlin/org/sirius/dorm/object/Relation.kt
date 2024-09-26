package org.sirius.dorm.`object`
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.RelationDescriptor
import org.sirius.dorm.persistence.entity.PropertyEntity

abstract class Relation(val relation: RelationDescriptor<*>, val targetDescriptor: ObjectDescriptor, property: PropertyEntity?) : Property(property) {
    abstract fun isLoaded(): Boolean

    abstract fun deleted();

    abstract fun load(objectManager: ObjectManager)

    override fun save(): Any {
        return this
    }

    override fun restore(state: Any) {
    }

    override fun isDirty(snapshot: Any) : Boolean {
        return isLoaded()
    }

    protected fun inverseRelation(obj: DataObject?) : Relation? {
        return if ( obj !== null && isLoaded()) {
            val relation = obj.values[relation.inverseRelation!!.index] as Relation
            if (relation.isLoaded()) relation else null
        }
        else null
    }

    fun relations() : MutableSet<PropertyEntity> {
        return if ( relation.isOwner()) property!!.targets else property!!.sources
    }

    // abstract

    abstract fun addedToInverse(element: DataObject)
    abstract fun removedFromInverse(element: DataObject)
}