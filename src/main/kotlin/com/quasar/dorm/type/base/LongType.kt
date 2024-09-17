package com.quasar.dorm.type.base
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import com.quasar.dorm.type.Type

class LongType : Type<Long>(Long::class.java) {
}

fun long() : LongType {return LongType()}