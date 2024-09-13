package com.example.dorm.type.base

import com.example.dorm.type.Test
import com.example.dorm.type.Type

class ShortType : Type<Short>(Short::class.java) {
  // TODO
}

fun short() : ShortType {return ShortType()}