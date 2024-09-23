package org.sirius.dorm.`object`
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.PropertyDescriptor
import org.sirius.dorm.model.RelationDescriptor
import org.sirius.dorm.persistence.entity.PropertyEntity
import org.sirius.dorm.transaction.TransactionState

class SingleValuedRelation(val relation: RelationDescriptor<*>, val obj: DataObject, property: PropertyEntity?, val targetDescriptor: ObjectDescriptor) : Relation(property) {
    // instance data

    var target: DataObject? = DataObject.NONE

    // private

    private fun isLoaded() : Boolean {
        return target !== DataObject.NONE
    }

    // override

    override fun isDirty(snapshot: Any) : Boolean {
        return isLoaded()
    }

    override fun flush() {
        if ( isLoaded()) {
            property!!.relations.clear()
            if ( target !== null) {
                property!!.relations.add(target!!.values[relation.inverseRelation!!.index].property!!)
            }
        }
    }
    override fun get(objectManager: ObjectManager) : Any? {
        if ( !isLoaded() && property !== null) {
            if ( property!!.relations.size == 1) {
                val targetProperty = property!!.relations.first()
                target = objectManager.mapper.read(TransactionState.current(), targetDescriptor, targetProperty.entity)
            }
            else
                target = null
        }

        return target
    }

    override fun set(propertyDescriptor: PropertyDescriptor<Any>, value: Any?) : Boolean {
        this.target = value as DataObject?

        return true
    }

    override fun save(): Any {
        return this
    }

    override fun restore(state: Any) {
    }
}