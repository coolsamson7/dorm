package org.sirius.common.type.base
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import org.sirius.common.type.Type

class StringType : Type<String>(String::class.java) {
    // fluent

    fun length(length: Int/*, info?: ConstraintInfo*/) : StringType {
        this.test<String>(
            "length",
            length,
            { obj -> obj.length <= length }
        )

        return this
    }

    // TODO: matches
}

fun string() : StringType {return StringType()
}