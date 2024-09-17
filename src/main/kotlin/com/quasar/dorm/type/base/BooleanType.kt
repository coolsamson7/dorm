package com.quasar.dorm.type.base
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.type.Type

class BooleanType : Type<Boolean>(Boolean::class.java) {
    fun isTrue() : BooleanType {
        test<Boolean>(
            "is-true",
            {  obj -> obj }
        )

        return this
    }

    fun isFalse() : BooleanType {
        test<Boolean>(
            "is-false",
            {  obj -> !obj }
        )

        return this
    }
}

fun boolean() : BooleanType {return BooleanType()}