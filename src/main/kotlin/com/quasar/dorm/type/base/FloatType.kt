package com.quasar.dorm.type.base
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import com.quasar.dorm.type.Type

class FloatType : Type<Float>(Float::class.java) {
}

fun float() : FloatType {return FloatType()}