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
import java.util.HashMap

class MultiValuedRelation(relation: RelationDescriptor<*>, val obj: DataObject, property: PropertyEntity?, targetDescriptor: ObjectDescriptor) : Relation(relation, targetDescriptor, property), MutableSet<DataObject> {
    // instance data

    private var objects : HashSet<DataObject>? = null

    // private

    private fun load(objectManager: ObjectManager) {
        objects = HashSet()
        for ( target in property!!.targets ) {
            objects!!.add(objectManager.mapper.read(TransactionState.current(), targetDescriptor, target.entity))
        }
    }

    private fun markDirty() {
        if ( obj.state!!.snapshot == null)
            obj.state!!.takeSnapshot(obj)
    }

    // implement Relation

    override fun isLoaded() : Boolean {
        return objects !== null
    }

    override fun addInverse(element: DataObject) {
        this.objects!!.add(element)
    }
    override fun removeInverse(element: DataObject) {
        this.objects!!.remove(element)
    }

    // implement  Property

    override fun get(objectManager: ObjectManager) : Any? {
        if ( !isLoaded())
            load(objectManager)

        return this
    }

    override fun set(propertyDescriptor: PropertyDescriptor<Any>, value: Any?) : Boolean  {
        throw Error("relations don't allow to be set")
    }

    override fun flush() {
        if (isLoaded()) {
            // synchronize the objects set with the property.relations

            val targetMap = HashMap<Int, PropertyEntity>()
            val relations = property!!.targets

            // collect all targets in a map

            for (previousTarget in relations)
                targetMap[previousTarget.entity.id] = previousTarget

            // iterate over source objects

            for (target in objects!!) {
                val key = target.id

                if (!targetMap.containsKey(key))
                    relations.add(target.values[relation.index].property!!)
                else
                    targetMap.remove(key)
            } // for

            // deleted

            for (deleted in targetMap.values) {
                relations.remove(deleted)
            } // if
        }
    }

    // implement MutableSet

    override fun add(element: DataObject): Boolean {
        markDirty()

        if (objects!!.add(element)) {
            // take care of inverse

            val inverse = inverseRelation(element)
            if ( inverse !== null) {
                inverse.addInverse(obj)
            }

            return true
        }
        else return false
    }

    override fun addAll(elements: Collection<DataObject>): Boolean {
        var result = false
        for (element in elements)
            if (add(element))
                result = true

        return result
    }

    override fun remove(element: DataObject): Boolean {
        markDirty()

        if (objects!!.remove(element)) {
            // take care of inverse

            val inverse = inverseRelation(element!!)
            if ( inverse !== null) {
                inverse.removeInverse(element)
            }

            return true
        }
        else return false
    }

    override val size: Int
        get() = objects!!.size

    override fun clear() {
        markDirty() // TODO

        return objects!!.clear()
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
        markDirty()

        return objects!!.retainAll(elements)
    }

    override fun removeAll(elements: Collection<DataObject>): Boolean {
        markDirty()

        return objects!!.removeAll(elements)
    }
}