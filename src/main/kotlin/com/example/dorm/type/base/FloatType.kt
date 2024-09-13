package com.example.dorm.type.base

import com.example.dorm.type.Test
import com.example.dorm.type.Type

class FloatType : Type<Float>(Float::class.java) {
}

fun float() : FloatType {return FloatType()}