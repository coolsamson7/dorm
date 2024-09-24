package org.sirius.common.type
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

typealias Check<T> = (obj: T) -> Boolean

typealias DefaultValue<T> = () -> T

open class Type<T:Any>(val baseType: Class<T>) {
    // instance data

    val tests = ArrayList<Test<Any>>()
    var sealed = false

    val defaultValue : DefaultValue<T> = computeDefaultValue(baseType)

    private fun <T> computeDefaultValue(type: Class<T>) : DefaultValue<T> {
        return when (type) {
            String::class.javaObjectType -> { -> "" as T }
            Short::class.javaObjectType -> { -> 0.toShort() as  T }
            Integer::class.javaObjectType -> { -> 0 as T }
            Int::class.javaObjectType -> { -> 0 as T }
            Long::class.javaObjectType -> { -> 0L as T }
            Float::class.javaObjectType -> { -> 0.0f as T }
            Double::class.javaObjectType -> { -> 0.0  as T }
            Boolean::class.javaObjectType -> { -> false as T }
            Char::class.javaObjectType -> { -> ' ' as T }
            else -> {
                throw Error("unsupported type ${type.simpleName}")
            }
        }
    }

    // init

    init {
        test<Any>(
            "type",
            baseType.simpleName,
            {  obj -> obj::class.java == baseType },
            true,
            false
        )
    }

    // public

    fun seal() : Type<T> {
        this.sealed = true

        return this
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

        if (context.violations != null)
            throw ValidationError(context.violations!!)
    }

    fun isValid(obj: Any): Boolean {
        val context = ValidationContext()

        this.check(obj, context)

        return context.violations == null
    }

    // private

    private fun check(obj: Any, context: ValidationContext) {
        for (test in tests)
            if (!test.check(obj)) {
                // remember violation

                if (!test.ignore)
                    context.addViolation(
                        TypeViolation(
                            test.type,
                            test.name,
                            test.parameter,
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

    protected fun <O> test(name: String, parameter: Any?, check: Check<O>, stop: Boolean = false, ignore: Boolean = false): Type<T> {
        if ( sealed )
            throw Error("type is sealed")

        tests.add(Test(baseType, name, parameter, check as Check<Any>, stop, ignore) as Test<Any>)

        return this
    }
}