package org.sirius.dorm.query.parser
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.query.*
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.TokenStream

// syntax tree classes

abstract class EXPR {
    open fun <T:Any> build(select: SELECT, query: Query<T>) : ObjectExpression {
        throw Error("NYI")
    }
}

abstract class VALUE(val value: Any) : EXPR() {
    abstract fun <T: Any> resolve(query: Query<T>) : Value
}

class PARAMETER(private val name: String) : VALUE(name) {
    // override

    override fun <T: Any> resolve(query: Query<T>) : Value {
        return query.parameter(name)
    }
}

class CONSTANT(value: Any) : VALUE(value) {
    override fun <T: Any> resolve(query: Query<T>) : Value {
        return Constant(value)
    }
}

open class FROM(@JvmField var alias : String) : EXPR() {
    // public
}

// person p
class ROOT(val schema: String, alias : String) : FROM(alias) {
    // public
}

// join p.hobbies
class JOIN(val root: String, val relation: String, alias : String) : FROM(alias) {
    // public
}


open class SELECT() {
    // instance data

    @JvmField
    var select : List<PATH> = ArrayList()
    @JvmField
    val from = HashMap<String,FROM>()
    @JvmField
    var where: BOOLEAN_EXPR? = null

    val alias = HashMap<String,AbstractFrom>()

    // protected

    fun addFrom( alias: String, from: FROM) {
        from.alias = alias

        if ( !this.from.containsKey(alias))
            this.from[alias] = from
        else
            throw Error("duplicate alias ${alias}")
    }

    // public

    fun <T:Any> transform(queryManager: QueryManager) : Query<T> {
        // find root

        val root = from.values.find { f -> f is ROOT } ?: throw Error("root is missing")

        // remember aliases

        alias[root.alias] = FromRoot(queryManager.objectManager.getDescriptor((root as ROOT).schema))

        for ( from in from.values)
            if ( from is JOIN) {
                val r = alias[from.root]
                if (r == null)
                    throw Error("unknown alias ${from.root}")

                alias[from.alias] = JoinFrom(r as FromRoot, from.relation)
            }

        // create query

        val from = alias.get(root.alias)

        val query = queryManager
            .query() // object query
            .select(*select.map { path -> path.buildPath(alias) }.toTypedArray())
            .from(from!!)

        if ( where !== null)
            query.where(where!!.build(this, query))

        return query as Query<T>
    }
}
abstract class PATH : EXPR() {
    // public

    fun property(property: String) : PROPERTY {
        return PROPERTY(this, property)
    }

    open fun buildPath(alias: HashMap<String,AbstractFrom>) : ObjectPath {
        throw Error("abstract")
    }
}

class PATH_ROOT(val schema: String) : PATH() {

    // override
    override fun buildPath(alias: HashMap<String,AbstractFrom>) : ObjectPath {
        if ( alias.containsKey(schema))
            return alias[schema]!!
        else throw Error("unknown alias ${schema}")
    }
}

class PROPERTY(val root: PATH, val property: String) : PATH() {
    override fun buildPath(alias: HashMap<String,AbstractFrom>) : ObjectPath {
        return root.buildPath(alias).get(property)
    }
}


abstract class BOOLEAN_EXPR : EXPR() {}

// arithmetic operators

class LT(val expression: PATH, val value: VALUE) : BOOLEAN_EXPR() {
    override fun <T:Any> build(select: SELECT, query: Query<T>) : ObjectExpression {
        return lt(expression.buildPath(select.alias), value.resolve(query))
    }
}

class LE(val expression: PATH, val value: VALUE) : BOOLEAN_EXPR() {
    override fun <T:Any> build(select: SELECT, query: Query<T>) : ObjectExpression {
        return le(expression.buildPath(select.alias), value.resolve(query))
    }
}

class GT(val expression: PATH, val value: VALUE) : BOOLEAN_EXPR() {
    override fun <T:Any> build(select: SELECT, query: Query<T>) : ObjectExpression {
        return gt(expression.buildPath(select.alias), value.resolve(query))
    }
}

class GE(val expression: PATH, val value: VALUE) : BOOLEAN_EXPR() {
    override fun <T:Any> build(select: SELECT, query: Query<T>) : ObjectExpression {
        return ge(expression.buildPath(select.alias), value.resolve(query))
    }
}

class EQ(val expression: PATH, val value: VALUE) : BOOLEAN_EXPR() {
    override fun <T:Any> build(select: SELECT, query: Query<T>) : ObjectExpression {
        return eq(expression.buildPath(select.alias), value.resolve(query))
    }
}

class NE(val expression: PATH, val value: VALUE) : BOOLEAN_EXPR() {
    override fun <T:Any> build(select: SELECT, query: Query<T>) : ObjectExpression {
        return ne(expression.buildPath(select.alias), value.resolve(query))
    }
}

// logical operators

class AND(vararg expr: BOOLEAN_EXPR) : BOOLEAN_EXPR() {
    val expressions = expr

    override fun <T:Any> build(select: SELECT, query: Query<T>) : ObjectExpression {
        return and(*expressions.map({expr -> expr.build(select, query) as BooleanExpression}).toTypedArray())
    }
}

class OR(vararg expr: BOOLEAN_EXPR) : BOOLEAN_EXPR() {
    val expressions = expr

    override fun <T:Any> build(select: SELECT, query: Query<T>) : ObjectExpression {
        return or(*expressions.map({expr -> expr.build(select, query) as BooleanExpression}).toTypedArray())
    }
}

abstract class AbstractQueryParser(input: TokenStream) : Parser(input) {
    // instance data

    @JvmField
    var select : SELECT? = null

    // protected

    protected fun comparison(path: PATH, operator: String, value: VALUE) : BOOLEAN_EXPR {
        return when (operator) {
            "=" -> EQ(path ,value)
            "<>" -> NE(path ,value)
            "<" -> LT(path ,value)
            ">" -> GT(path ,value)
            "<=" -> LE(path ,value)
            ">=" -> GE(path ,value)

            else -> {
                throw Error("unsupported operator ${operator}")
            }
        }
    }
}