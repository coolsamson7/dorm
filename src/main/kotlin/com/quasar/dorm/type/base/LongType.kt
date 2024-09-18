package com.quasar.dorm.type.base
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import com.quasar.dorm.type.Type

class LongType : Type<Long>(Long::class.java) {
    fun min(min: Long) : LongType {
        test<Long>(
            "min",
            min,
            {  obj -> obj >= min }
        )

        return this
    }

    fun max(max: Long) : LongType {
        test<Long>(
            "max",
            max,
            {  obj -> obj <= max }
        )

        return this
    }

    fun lessThan(value: Long) : LongType {
        test<Long>(
            "lessThan",
            value,
            {  obj -> obj < value }
        )

        return this
    }

    fun lessEqual(value: Long) : LongType {
        test<Long>(
            "lessEqual",
            value,
            {  obj -> obj <= value }
        )

        return this
    }

    fun greaterThan(value: Long) : LongType {
        test<Long>(
            "greaterThan",
            value,
            {  obj -> obj > value }
        )

        return this
    }

    fun greaterEqual(value: Long) : LongType {
        test<Long>(
            "greaterEqual",
            value,
            {  obj -> obj >= value }
        )

        return this
    }
}

fun long() : LongType {return LongType()}