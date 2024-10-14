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
import org.sirius.dorm.transaction.*
import java.util.HashMap

class MultiValuedRelation(relation: RelationDescriptor<*>, val obj: DataObject, property: PropertyEntity?, targetDescriptor: ObjectDescriptor) : Relation(relation, targetDescriptor, property), MutableSet<DataObject> {
    // instance data

    private var objects : HashSet<DataObject>? = if ( obj.state.isSet(Status.CREATED)) HashSet() else null

    // private

    override fun load(objectManager: ObjectManager) {
        objects = HashSet()
        if ( property !== null) {
            val transactionState = TransactionState.current()
            for (target in relations())
                objects!!.add(objectManager.mapper.read(transactionState, targetDescriptor, target.entity))

            // redo anything related to me

            transactionState.checkRedos(this.obj.id, relation.name, this)
        }
    }

    private fun markDirty() {
        if ( obj.state !== null)
            if ( obj.state?.snapshot == null)
                obj.state?.takeSnapshot(obj)
    }

    // implement Relation

    override fun deleted() {
        for ( obj in this.objects!!)
            obj.delete()
    }

    override fun isLoaded() : Boolean {
        return objects !== null
    }
    override fun addedToInverse(element: DataObject) {
        this.objects!!.add(element)
    }
    override fun removedFromInverse(element: DataObject) {
        this.objects!!.remove(element)
    }

    // implement  Property

    override fun <T:Any>  get(objectManager: ObjectManager) : T? {
        if ( !isLoaded())
            load(objectManager)

        return this as T
    }

    override fun set(propertyDescriptor: PropertyDescriptor<Any>, value: Any?,  objectManager: ObjectManager) : Boolean  {
        throw Error("relations don't allow to be set")
    }

    override fun flush() {
        if (isLoaded()) {
            // synchronize the objects set with the property.relations

            val targetMap = HashMap<Long, PropertyEntity>()
            val relations = relations()

            // collect all targets in a map

            for (previousTarget in relations)
                targetMap[previousTarget.entity.id] = previousTarget

            // iterate over source objects

            for (target in objects!!) {
                val key = target.id

                if (!targetMap.containsKey(key)) {
                    val property = target.values[relation.inverseRelation!!.index].property!!

                    relations.add(property)
                }
                else
                    targetMap.remove(key)
            } // for

            // deleted

            for (deleted in targetMap.values) {
                relations.remove(deleted)

                if ( relation.cascadeDelete) {
                    // TODO: check cascading
                    deleted.entity.id
                }

                if ( !this.relation.isOwner()) {
                    deleted.targets.remove(this.property)
                }
            } // if
        }
    }

    // implement MutableSet

    override fun add(element: DataObject): Boolean {
        markDirty()

        return if (objects!!.add(element)) {
            // take care of inverse

            val inverse = inverseRelation(element)
            if ( inverse !== null)
                inverse.addedToInverse(obj)
            else
                TransactionState.current().addRedo(element.id, relation.inverseRelation!!.name, AddToRelation(this.obj))

            true
        }
        else false
    }

    override fun addAll(elements: Collection<DataObject>): Boolean {
        var result = false
        for (element in elements)
            if (add(element))
                result = true

        return result
    }

    override fun remove(element: DataObject): Boolean {
        return if (objects!!.remove(element)) {
            markDirty()

            // take care of inverse

            val inverse = inverseRelation(element)
            if ( inverse !== null)
                inverse.removedFromInverse(element)
            else
                TransactionState.current().addRedo(element.id, relation.inverseRelation!!.name, RemoveFromRelation(this.obj))

            true
        }
        else false
    }

    override val size: Int
        get() = objects!!.size

    override fun clear() {
        for (element in this.objects!!)
            remove(element)
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun containsAll(elements: Collection<DataObject>): Boolean {
        return objects!!.containsAll(elements)
    }

    override fun contains(element: DataObject): Boolean {
        return objects!!.contains(element)
    }

    override fun iterator(): MutableIterator<DataObject> {
        return objects!!.iterator()
    }

    override fun retainAll(elements: Collection<DataObject>): Boolean {
        var result = false
        for ( element in objects!!)
            if ( !elements.contains(element))
                result = remove(element)

        return result
    }

    override fun removeAll(elements: Collection<DataObject>): Boolean {
        var result = false
        for ( element in elements)
            if (remove(element))
                result = true

        return result
    }
}