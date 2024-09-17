package com.example.dorm.type.base
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import com.example.dorm.type.Test
import com.example.dorm.type.Type

class FloatType : Type<Float>(Float::class.java) {
}

fun float() : FloatType {return FloatType()}