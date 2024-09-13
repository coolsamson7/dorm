package com.example.dorm.type.base

import com.example.dorm.type.Test
import com.example.dorm.type.Type

class LongType : Type<Long>(Long::class.java) {
}

fun long() : LongType {return LongType()}