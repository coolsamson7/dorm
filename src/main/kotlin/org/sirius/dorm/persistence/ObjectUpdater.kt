package org.sirius.dorm.persistence
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.`object`.DataObject
import org.sirius.dorm.transaction.TransactionState

class ObjectUpdater(descriptor: ObjectDescriptor) : ObjectWriter(descriptor) {
    // public

    fun update(state: TransactionState, obj: DataObject) {
        var i = 0
        val snapshot = obj.state!!.snapshot!!
        for ( writer in writer) {
            val property = obj.values[i]

            if (!descriptor.properties[i].readOnly && property.isDirty(snapshot[i]))
                writer(state, obj, i, property.property!!)

            i++
        }
    }
}