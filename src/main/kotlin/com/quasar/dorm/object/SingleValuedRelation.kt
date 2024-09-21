package com.quasar.dorm.`object`

import com.quasar.dorm.ObjectManager
import com.quasar.dorm.model.ObjectDescriptor
import com.quasar.dorm.model.PropertyDescriptor
import com.quasar.dorm.persistence.entity.AttributeEntity

class SingleValuedRelation(property: AttributeEntity?, val targetDescriptor: ObjectDescriptor) : Relation(property) {
    // instance data

    var target: DataObject? = DataObject.NONE

    // private

    private fun isLoaded() : Boolean {
        return target !== DataObject.NONE
    }

    // override

    override fun flush() {
        if ( isLoaded()) {
            property!!.relations.clear()
            if ( target !== null) {
                property!!.relations.add(target!!.entity!!)
            }
        }
    }
    override fun get(objectManager: ObjectManager) : Any? {
        if ( !isLoaded() && property !== null) {
            if ( property!!.relations.size == 1) {
                val targetEntity = property!!.relations.first()
                target = objectManager.mapper.read(objectManager.transactionState(), targetDescriptor, targetEntity)
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