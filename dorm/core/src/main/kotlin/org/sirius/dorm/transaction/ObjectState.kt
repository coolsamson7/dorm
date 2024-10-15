package org.sirius.dorm.transaction
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.`object`.DataObject
import java.util.*

class ObjectState(val obj: DataObject, vararg status: Status) : BitSet(4) {
    // instance data

    var snapshot: List<Any>? = null

    // init

    init {
        for ( value in status)
            set(value)
    }

    // public

    fun set(status: Status) {
        super.set(status.intValue)
    }

    fun clear(status: Status) {
        super.clear(status.intValue)
    }

    fun isSet(status: Status) : Boolean {
        return this[status.intValue]
    }

    fun rollback() {
        //TODO RELATION for ( i in 0..obj.values.size-1)
        //    obj.values[i] = snapshot!![i]

        snapshot = null
        set(Status.MANAGED)
    }

    fun isDirty() : Boolean {
        if (!isSet(Status.MANAGED))
            return false

        if (snapshot !== null) {
            for ( i in 0..<snapshot!!.size)
                if (obj.values[i].isDirty(snapshot!![i]))
                    return true
        }

        return false
    }

    fun modified() {
        set(Status.MODIFIED)
        takeSnapshot(obj)
    }

    fun takeSnapshot(obj: DataObject) {
      if ( isSet(Status.MANAGED) && snapshot == null)
            snapshot = obj.snapshot()
    }

    // override Object

    override fun toString(): String {
        val builder = StringBuilder("${obj.type.name} {")

        var first = true
        for ( value in Status.values()) {
            if (isSet(value)) {
                if ( !first)
                    builder.append(", ")

                builder.append(value.toString())
                first = false
            }
        }

        builder.append("}")

        return builder.toString()
    }
}