package com.quasar.dorm.transaction
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.DataObject
import com.quasar.dorm.model.PropertyDescriptor

class ObjectState(val obj: DataObject, var status: Status) {
    // instance data

    var snapshot: Array<Any?>? = null

    // init

    init {
        obj.state = this
    }

    // public

    fun rollback() {
        for ( i in 0..obj.values.size-1)
            obj.values[i] = snapshot!![i]

        snapshot = null
        status = Status.MANAGED
    }

    fun isDirty() : Boolean {
        if (status != Status.MANAGED)
            return false

        if (snapshot !== null) {
            for ( i in 0..snapshot!!.size)
                if (obj.values[i] != snapshot!![i])
                    return true
        }

        return false
    }

    fun setValue(obj: DataObject, property: PropertyDescriptor<Any>, value: Any) {
        // validate

        property.validate(value)

        // take snapshot

        if ( status == Status.MANAGED && value !== obj.values[property.index] && snapshot == null)
            snapshot = obj.values.clone()
    }
}