package com.example.dorm.type.base

import com.example.dorm.type.Type

class StringType : Type<String>(String::class.java) {
    // fluent

    fun length(length: Int/*, info?: ConstraintInfo*/) : StringType {
        this.test<String>(
            "length",
            //params: {
            //    length: length,
            //},
            //...info,
            { obj -> obj.length <= length }
        )

        return this
    }
}

fun string() : StringType {return StringType()}