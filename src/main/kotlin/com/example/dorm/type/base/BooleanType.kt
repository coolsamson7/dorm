package com.example.dorm.type.base

import com.example.dorm.type.Test
import com.example.dorm.type.Type

class BooleanType : Type<Boolean>(Boolean::class.java) {
}

fun boolean() : BooleanType {return BooleanType()}