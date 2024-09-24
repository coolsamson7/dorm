package org.sirius.dorm.`object`
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.ObjectManager
import org.sirius.dorm.ObjectManagerError
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.PropertyDescriptor
import org.sirius.dorm.model.RelationDescriptor
import org.sirius.dorm.persistence.entity.PropertyEntity
import org.sirius.dorm.transaction.Status
import org.sirius.dorm.transaction.TransactionState

class SingleValuedRelation(relation: RelationDescriptor<*>, status: Status, val obj: DataObject, property: PropertyEntity?, targetDescriptor: ObjectDescriptor) : Relation(relation, targetDescriptor, property) {
    // instance data

    var target: DataObject? = if ( status == Status.CREATED ) null else DataObject.NONE

    // implement Relation

    override fun deleted() {
        target?.delete()
    }

    override fun load(objectManager: ObjectManager) {
       if ( property !== null) {
            if ( relations().size == 1) {
                val targetProperty = relations().first()
                target = objectManager.mapper.read(TransactionState.current(), targetDescriptor, targetProperty.entity)
            }
            else
                target = null
        }
        else target = null
    }

    override fun isLoaded() : Boolean {
        return target !== DataObject.NONE
    }

    // override

    override fun flush() {
        if ( isLoaded()) {
            relations().clear()
            if ( target !== null) {
                relations().add(target!!.values[relation.inverseRelation!!.index].property!!)
            }
            else {
                if ( !relation.multiplicity.optional)
                    throw ObjectManagerError("relation ${obj.type.name}.${relation.name} is required")
            }
        }
    }
    override fun get(objectManager: ObjectManager) : Any? {
        if ( !isLoaded())
            load(objectManager)

        return target
    }

    override fun set(propertyDescriptor: PropertyDescriptor<Any>, value: Any?) : Boolean {
        if ( value !== this.target) {
            var inverse = inverseRelation(this.target)
            if ( inverse !== null)
                inverse.removeInverse(this.obj)

            this.target = value as DataObject?

            if ( this.target !== null) {
                inverse = inverseRelation(this.target)
                if ( inverse !== null)
                    inverse.addInverse(this.obj)
            }

            // done

            return true
        }
        else return false
    }

    override fun addInverse(element: DataObject) {
        this.target = element
    }
    override fun removeInverse(element: DataObject) {
        this.target = null
    }
}