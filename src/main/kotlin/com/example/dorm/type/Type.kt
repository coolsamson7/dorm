package com.example.dorm.type
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

typealias Check<T> = (obj: T) -> Boolean

typealias DefaultValue<T> = () -> T

open class Type<T:Any>(val baseType: Class<T>) {
    // instance data

    protected val tests = ArrayList<Test<Any>>()

    val defaultValue : DefaultValue<T> = computeDefaultValue()

    fun <T:Any> computeDefaultValue() : DefaultValue<T> {
        return when (baseType) {
            String::class.java -> { -> "" as T }
            Short::class.java -> { -> 0 as  T }
            Integer::class.java -> { -> 0 as T }
            Long::class.java -> { -> 0L as T }
            Float::class.java -> { -> 0.0 as T }
            Double::class.java -> { -> 0.0 as T }
            Boolean::class.java -> { -> false as T }
            else -> {
                throw Error("unsupported type")
            }
        }
    }

    // init

    init {
        test<Any>(
            "type",
            //params: {
            //    type: type,
            //},
            {  obj -> obj::class.java == baseType },
            true,
            false
        )
    }

    // fluent

    protected fun required(): Type<T> {
        val typeTest = this.tests[0]

        typeTest.ignore = false

        return this
    }

    protected fun optional(): Type<T> {
        val typeTest = this.tests[0]

        typeTest.ignore = true

        return this
    }

    // public

    fun validate(obj: Any) {
        val context = ValidationContext()

        this.check(obj, context)

        if (context.violations.size > 0)
            throw ValidationError(context.violations)
    }

    fun isValid(obj: Any): Boolean {
        val context = ValidationContext()

        this.check(obj, context)

        return context.violations.size == 0
    }

    // private

    private fun check(obj: Any, context: ValidationContext) {
        for (test in tests)
            if (!test.check(obj)) {
                // remember violation

                if (!test.ignore)
                    context.violations.add(
                        TypeViolation(
                            test.type,
                            test.name,
                            //params: test.params,
                            obj,
                            context.path,
                            //message: test.message,
                        )
                    )

                if (test.stop)
                    return
            }
    }

    // protected

    protected fun <O> test(name: String, check: Check<O>, stop: Boolean = false, ignore: Boolean = false): Type<T> {
        tests.add(Test(baseType, name, check as Check<Any>, stop, ignore) as Test<Any>)

        return this
    }
}