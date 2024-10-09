package org.sirius.dorm.persistence
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.`object`.DataObject
import org.sirius.dorm.persistence.entity.PropertyEntity
import org.sirius.dorm.transaction.TransactionState
import jakarta.persistence.EntityManager

typealias PropertyWriter = (state: TransactionState, obj: DataObject, property: Int, attribute: PropertyEntity) -> Unit

class ObjectCreator(descriptor: ObjectDescriptor) : ObjectWriter(descriptor) {
    // public

    fun create(state: TransactionState, obj: DataObject, entityManager: EntityManager) {
        var i = 0
        for ( writer in writer) {
            val propertyDescriptor = descriptor.properties[i]

            if ( !propertyDescriptor.readOnly) { // the rest is part of the entity
                val property = PropertyEntity(obj.entity!!, propertyDescriptor.name, descriptor.name, "", 0, 0.0)

                obj.values[i].property = property

                writer(state, obj, i, property)

                entityManager.persist(property)
            }

            i++
        }
    }
}