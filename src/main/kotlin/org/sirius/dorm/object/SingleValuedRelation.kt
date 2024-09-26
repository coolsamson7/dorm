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
import org.sirius.dorm.transaction.AddToRelation
import org.sirius.dorm.transaction.RemoveFromRelation
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
           // start with the persistent state

            if ( relations().size == 1) {
                val targetProperty = relations().first()
                target = objectManager.mapper.read(TransactionState.current(), targetDescriptor, targetProperty.entity)
            }
            else
                target = null

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

            val relationenEntities = relations()

            val n = if (relation.isOwner() ) "targets" else "sources"

            if ( target === null) {
                if ( relationenEntities.isNotEmpty()) {
                    if ( relationenEntities.size > 1)
                        throw Error("iuch")

                    val property = relationenEntities.first()

                    relationenEntities.clear()

                    println("remove ${property.entity.id}.${property.attribute} from entity[${obj.id}].${relation.name}.${n}")
                }
            }
            else {
                if ( relationenEntities.size == 1) {
                    val property = relationenEntities.first()
                    if (property.entity !== target!!.entity ) {
                        println("add ${property.entity.id}.${property.attribute} to entity[${obj.id}].${relation.name}.${n}")
                        relationenEntities.add(property)
                    }
                }

                if (relationenEntities.size == 0) {
                    val property = target!!.values[relation.inverseRelation!!.index].property!!
                    println("add ${property.entity.id}.${property.attribute} to entity[${obj.id}].${relation.name}.${n}")
                    relationenEntities.add(property)
                }
                else  throw Error("iuch")
            }
        }
    }
    override fun get(objectManager: ObjectManager) : Any? {
        if ( !isLoaded())
            load(objectManager)

        return target
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