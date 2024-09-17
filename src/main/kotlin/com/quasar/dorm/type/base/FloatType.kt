package com.quasar.dorm.type.base
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import com.quasar.dorm.type.Type

class FloatType : Type<Float>(Float::class.java) {
    fun min(min: Float) : FloatType {
        test<Float>(
            "min",
            {  obj -> obj >= min }
        )

        return this
    }

    fun max(max: Float) : FloatType {
        test<Float>(
            "max",
            {  obj -> obj <= max }
        )

        return this
    }

    fun lessThan(value: Float) : FloatType {
        test<Float>(
            "lessThan",
            {  obj -> obj < value }
        )

        return this
    }

    fun lessEqual(value: Float) : FloatType {
        test<Float>(
            "lessEqual",
            {  obj -> obj <= value }
        )

        return this
    }

    fun greaterThan(value: Float) : FloatType {
        test<Float>(
            "greaterThan",
            {  obj -> obj > value }
        )

        return this
    }

    fun greaterEqual(value: Float) : FloatType {
        test<Float>(
            "greaterEqual",
            {  obj -> obj >= value }
        )

        return this
    }
}

fun float() : FloatType {return FloatType()}