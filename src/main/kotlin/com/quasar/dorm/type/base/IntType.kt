package com.quasar.dorm.type.base
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import com.quasar.dorm.type.Type

class IntType : Type<Integer>(Integer::class.java) {
    // fluent

    fun min(min: Int) : IntType {
        test<Integer>(
            "min",
            {  obj -> obj >= min }
        )

        return this
    }

    fun max(max: Int) : IntType {
        test<Integer>(
            "max",
            {  obj -> obj <= max }
        )

        return this
    }

    fun lessThan(value: Int) : IntType {
        test<Integer>(
            "lessThan",
            {  obj -> obj < value }
        )

        return this
    }

    fun lessEqual(value: Int) : IntType {
        test<Integer>(
            "lessEqual",
            {  obj -> obj <= value }
        )

        return this
    }

    fun greaterThan(value: Int) : IntType {
        test<Integer>(
            "greaterThan",
            {  obj -> obj > value }
        )

        return this
    }

    fun greaterEqual(value: Int) : IntType {
        test<Integer>(
            "greaterEqual",
            {  obj -> obj >= value }
        )

        return this
    }
}

fun int() : IntType {return IntType()}