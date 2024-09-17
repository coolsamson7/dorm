package com.example.dorm.query
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

abstract class Value() {
    abstract fun <T> resolve(executor: QueryExecutor<Any>, type: Class<T>) : T

    fun <T> checkType(type: Class<T>, value: Any, valueMessage: String) : T {
        if (type.isAssignableFrom(value.javaClass))
            return value as T
        else
            throw Error("expected ${valueMessage} to a ${type.name}")
    }
}

class Parameter(val name: String) : Value() {
    override fun <T> resolve(executor: QueryExecutor<Any>, type: Class<T>) : T {
        return checkType(type, executor.resolveParameter(name), "parameter ${name}")
    }
}

class Constant(private val value: Any) : Value() {
    override fun <T> resolve(executor: QueryExecutor<Any>, type: Class<T>) : T {
        return checkType(type, value, "constant ")
    }
}