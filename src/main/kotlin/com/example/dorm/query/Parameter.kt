package com.example.dorm.query
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

abstract class Value<T:Any>(protected val type: Class<T>) {
    abstract fun resolve(executor: QueryExecutor<Any>) : T
}

class Parameter<T:Any>(val name: String, type: Class<T>) : Value<T>(type) {
    override fun resolve(executor: QueryExecutor<Any>) : T {
        return executor.resolveParameter(name) as T
    }

    fun checkType(value: T) {
        if (!type.isAssignableFrom(value::class.java))
            throw Error("expected parameter ${name} to be a ${type.name}")
    }
}

class Constant<T:Any>(private val value: T) : Value<T>(value.javaClass) {
    override fun resolve(executor: QueryExecutor<Any>) : T {
        return value as T // TODO: type safeness?
    }
}