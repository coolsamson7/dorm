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
import org.sirius.dorm.transaction.*

class SingleValuedRelation(relation: RelationDescriptor<*>, val obj: DataObject, property: PropertyEntity?, targetDescriptor: ObjectDescriptor) : Relation(relation, targetDescriptor, property) {
    // instance data

    var target: DataObject? = if ( obj.state.isSet(Status.CREATED) ) null else DataObject.NONE

    // implement Relation

    override fun deleted() {
        target?.delete()
    }

    override fun load(objectManager: ObjectManager) {
       if ( property !== null) {
           // start with the persistent state

           target = if ( relations().size == 1) {
               val targetProperty = relations().first()
               objectManager.mapper.read(TransactionState.current(), targetDescriptor, targetProperty.entity)
           }
           else
               null

           // redo anything related to me

           TransactionState.current().checkRedos(this.obj.id, relation.name, this)
        }
        else target = null
    }

    override fun isLoaded() : Boolean {
        return target !== DataObject.NONE
    }

    // override

    override fun validate() {
        if ( target === null &&  !relation.multiplicity.optional)
            throw ObjectManagerError("relation ${obj.type.name}.${relation.name} is required")
    }

    override fun flush() {
        if ( isLoaded()) {
            // validate

            validate()

            // adjust relation

            val rel = relations()

            if ( target === null) {
                if ( rel.isNotEmpty()) {
                    val property = rel.first()

                    rel.remove(property)

                    if ( !relation.isOwner()) {
                        property.targets.remove(this.property)
                    }
               }
            }
            else {
                if ( rel.size == 1) {
                    val property = rel.first()
                    if (property.entity !== target!!.entity ) {
                        rel.remove(property)
                    }
                }

                if (rel.size == 0) {
                    val property = target!!.values[relation.inverseRelation!!.index].property!!
                    rel.add(property)
                }
            }
        }
    }
    override fun <T: Any>get(objectManager: ObjectManager) : T? {
        if ( !isLoaded())
            load(objectManager)

        return target as T?
    }

    override fun set(propertyDescriptor: PropertyDescriptor<Any>, value: Any?,  objectManager: ObjectManager) : Boolean {
        // check loaded

        if ( !isLoaded())
            load(objectManager)

        if ( value !== this.target) {
            var inverse = inverseRelation(this.target)
            if ( inverse !== null)
                inverse.removedFromInverse(this.obj)
            else if ( this.target !== null)
                TransactionState.current().addRedo(this.target!!.id, relation.inverseRelation!!.name, RemoveFromRelation(this.obj))

            this.target = value as DataObject?

            if ( this.target !== null) {
                inverse = inverseRelation(this.target)
                if ( inverse !== null)
                    inverse.addedToInverse(this.obj)
                else
                    TransactionState.current().addRedo(this.target!!.id, relation.inverseRelation!!.name, AddToRelation(this.obj))
            }

            // done

            return true
        }
        else return false
    }

    override fun addedToInverse(element: DataObject) {
        this.target = element
    }
    override fun removedFromInverse(element: DataObject) {
        this.target = null
    }
}