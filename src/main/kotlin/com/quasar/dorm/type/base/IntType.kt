package com.quasar.dorm.type.base
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import com.quasar.dorm.type.Type

class IntType : Type<Int>(Int::class.javaObjectType) {
    // fluent

    fun min(min: Int) : IntType {
        test<Int>(
            "min",
            min,
            {  obj -> obj >= min }
        )

        return this
    }

    fun max(max: Int) : IntType {
        test<Int>(
            "max",
            max,
            {  obj -> obj <= max }
        )

        return this
    }

    fun lessThan(value: Int) : IntType {
        test<Int>(
            "lessThan",
            value,
            {  obj -> obj < value }
        )

        return this
    }

    fun lessEqual(value: Int) : IntType {
        test<Int>(
            "lessEqual",
            value,
            {  obj -> obj <= value }
        )

        return this
    }

    fun greaterThan(value: Int) : IntType {
        test<Int>(
            "greaterThan",
            value,
            {  obj -> obj > value }
        )

        return this
    }

    fun greaterEqual(value: Int) : IntType {
        test<Int>(
            "greaterEqual",
            value,
            {  obj -> obj >= value }
        )

        return this
    }
}

fun int() : IntType {return IntType()}