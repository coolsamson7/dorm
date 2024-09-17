package com.quasar.dorm.type.base
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import com.quasar.dorm.type.Type

class DoubleType : Type<Double>(Double::class.java) {
    fun min(min: Double) : DoubleType {
        test<Double>(
            "min",
            {  obj -> obj >= min }
        )

        return this
    }

    fun max(max: Double) : DoubleType {
        test<Double>(
            "max",
            {  obj -> obj <= max }
        )

        return this
    }

    fun lessThan(value: Double) : DoubleType {
        test<Double>(
            "lessThan",
            {  obj -> obj < value }
        )

        return this
    }

    fun lessEqual(value: Double) : DoubleType {
        test<Double>(
            "lessEqual",
            {  obj -> obj <= value }
        )

        return this
    }

    fun greaterThan(value: Double) : DoubleType {
        test<Double>(
            "greaterThan",
            {  obj -> obj > value }
        )

        return this
    }

    fun greaterEqual(value: Double) : DoubleType {
        test<Double>(
            "greaterEqual",
            {  obj -> obj >= value }
        )

        return this
    }
}

fun double() : DoubleType {return DoubleType()}