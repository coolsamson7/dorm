package com.example.dorm.type.base

import com.example.dorm.type.Type

class IntType : Type<Integer>(Integer::class.java) {
    // fluent

    fun min(min: Int) : IntType {
        test<Integer>(
            "min",
            //params: { // TODO -> Keywords!!!!
            //    type: type,
            //},
            {  obj -> obj >= min }
        )

        return this
    }

    fun max(max: Int) : IntType {
        test<Integer>(
            "max",
            //params: {
            //    type: type,
            //},
            {  obj -> obj <= max }
        )

        return this
    }
}

fun int() : IntType {return IntType()}