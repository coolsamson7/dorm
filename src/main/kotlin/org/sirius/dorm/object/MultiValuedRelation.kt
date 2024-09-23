package org.sirius.dorm.`object`
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.PropertyDescriptor
import org.sirius.dorm.persistence.entity.AttributeEntity
import org.sirius.dorm.persistence.entity.EntityEntity
import org.sirius.dorm.transaction.TransactionState
import java.util.HashMap

class MultiValuedRelation(val obj: DataObject, property: AttributeEntity?, val targetDescriptor: ObjectDescriptor) : Relation(property), MutableSet<DataObject> {
    // instance data

    private var objects : HashSet<DataObject>? = null

    // private

    private fun isLoaded() : Boolean {
        return objects !== null
    }

    private fun load(objectManager: ObjectManager) {
        objects = HashSet()
        for ( target in property!!.relations ) {
            objects!!.add(objectManager.mapper.read(TransactionState.current(), targetDescriptor, target))
        }
    }

    private fun markDirty() {
        if ( obj.state!!.snapshot == null)
            obj.state!!.takeSnapshot(obj)
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

    override fun save(): Any {
        return this
    }

    override fun restore(state: Any) {
    }

    override fun isDirty(snapshot: Any) : Boolean {
        return isLoaded()
    }

    override fun flush() {
        if (isLoaded()) {
            // synchronize the objects set with the property.relations

            val targetMap = HashMap<Int, EntityEntity>()
            val relations = property!!.relations

            // collect all targets in a map

            for (previousTarget in relations)
                targetMap[previousTarget.id] = previousTarget

            // iterate over source objects

            for (target in objects!!) {
                val key = target.id

                if (!targetMap.containsKey(key))
                    relations.add(target.entity!!)
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

        return objects!!.add(element)
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

        return objects!!.remove(element)
    }

    override val size: Int
        get() = objects!!.size

    override fun clear() {
        markDirty()

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