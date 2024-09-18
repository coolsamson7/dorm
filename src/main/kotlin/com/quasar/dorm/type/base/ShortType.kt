package com.quasar.dorm.type.base
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import com.quasar.dorm.type.Type

class ShortType : Type<Short>(Short::class.java) {
    fun min(min: Short) : ShortType {
        test<Short>(
            "min",
            min,
            {  obj -> obj >= min }
        )

        return this
    }

    fun max(max: Short) : ShortType {
        test<Short>(
            "max",
            max,
            {  obj -> obj <= max }
        )

        return this
    }

    fun lessThan(value: Short) : ShortType {
        test<Short>(
            "lessThan",
            value,
            {  obj -> obj < value }
        )

        return this
    }

    fun lessEqual(value: Short) : ShortType {
        test<Short>(
            "lessEqual",
            value,
            {  obj -> obj <= value }
        )

        return this
    }

    fun greaterThan(value: Short) : ShortType {
        test<Short>(
            "greaterThan",
            value,
            {  obj -> obj > value }
        )

        return this
    }

    fun greaterEqual(value: Short) : ShortType {
        test<Short>(
            "greaterEqual",
            value,
            {  obj -> obj >= value }
        )

        return this
    }
}

fun short() : ShortType {return ShortType()}