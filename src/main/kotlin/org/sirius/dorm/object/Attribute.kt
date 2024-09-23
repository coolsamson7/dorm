package org.sirius.dorm.`object`

import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.PropertyDescriptor
import org.sirius.dorm.persistence.entity.AttributeEntity

class Attribute(property: AttributeEntity?, var value: Any) : Property(property) {
    override fun get(objectManager: ObjectManager) : Any? {
        return value
    }

    override fun init(propertyDescriptor: PropertyDescriptor<Any>, value: Any?) {
        this.value = value!!
    }

    override fun set(propertyDescriptor: PropertyDescriptor<Any>, value: Any?) : Boolean {
        propertyDescriptor.validate(value)

        if ( value != this.value ) {
            this.value = value!!

            return true
        }
        else return false
    }

    override fun save(): Any {
        return value
    }

    override fun restore(state: Any) {
        // TODO
    }

    override fun isDirty(snapshot: Any) : Boolean {
        return value != snapshot
    }
}