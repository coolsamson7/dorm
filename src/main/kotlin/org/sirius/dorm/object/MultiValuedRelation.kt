package org.sirius.dorm.`object`

import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.PropertyDescriptor
import org.sirius.dorm.persistence.entity.AttributeEntity

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