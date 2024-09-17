package com.example.dorm.type.base
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import com.example.dorm.type.Test
import com.example.dorm.type.Type

class DoubleType : Type<Double>(Double::class.java) {
}

fun double() : DoubleType {return DoubleType()}