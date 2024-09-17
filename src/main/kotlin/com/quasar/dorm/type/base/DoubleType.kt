package com.quasar.dorm.type.base
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import com.quasar.dorm.type.Type

class DoubleType : Type<Double>(Double::class.java) {
}

fun double() : DoubleType {return DoubleType()}