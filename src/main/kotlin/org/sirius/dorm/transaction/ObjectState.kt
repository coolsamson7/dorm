package org.sirius.dorm.transaction
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.`object`.DataObject

class ObjectState(val obj: DataObject, var status: Status) {
    // instance data

    var snapshot: List<Any>? = null

    // init

    init {
        obj.state = this
    }

    // public

    fun rollback() {
        //TODO RELATION for ( i in 0..obj.values.size-1)
        //    obj.values[i] = snapshot!![i]

        snapshot = null
        status = Status.MANAGED
    }

    fun isDirty() : Boolean {
        if (status != Status.MANAGED)
            return false

        if (snapshot !== null) {
            for ( i in 0..<snapshot!!.size)
                if (obj.values[i].isDirty(snapshot!![i]))
                    return true
        }

        return false
    }

    fun takeSnapshot(obj: DataObject) {
      if ( status == Status.MANAGED && snapshot == null)
            snapshot = obj.snapshot()
    }
}