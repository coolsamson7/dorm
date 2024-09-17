package com.example.dorm.type.base
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.example.dorm.type.Type

class BooleanType : Type<Boolean>(Boolean::class.java) {
}

fun boolean() : BooleanType {return BooleanType()}