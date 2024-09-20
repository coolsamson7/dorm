package com.quasar.dorm.type.base
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.type.Type

class BooleanType : Type<Boolean>(Boolean::class.javaObjectType) {
    fun isTrue() : BooleanType {
        test<Boolean>(
            "isTrue",
            null,
            {  obj -> obj }
        )

        return this
    }

    fun isFalse() : BooleanType {
        test<Boolean>(
            "isFalse",
            null,
            {  obj -> !obj }
        )

        return this
    }
}

fun boolean() : BooleanType {return BooleanType()}