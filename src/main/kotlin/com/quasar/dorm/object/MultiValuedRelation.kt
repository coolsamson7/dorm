package com.quasar.dorm.`object`

import com.quasar.dorm.ObjectManager
import com.quasar.dorm.model.ObjectDescriptor
import com.quasar.dorm.model.PropertyDescriptor
import com.quasar.dorm.persistence.entity.AttributeEntity

class MultiValuedRelation(property: AttributeEntity?, targetDescriptor: ObjectDescriptor) : Relation(property) {
    override fun get(objectManager: ObjectManager) : Any? {
        return null // TODO RELATION
    }

    override fun set(propertyDescriptor: PropertyDescriptor<Any>, value: Any?) : Boolean  {
        // TODO RELATIONthis.value = value!!
        return true
    }

    override fun save(): Any {
        return this
    }

    override fun restore(state: Any) {}
}