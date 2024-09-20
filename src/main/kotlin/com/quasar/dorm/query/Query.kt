package com.quasar.dorm.query
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.model.ObjectDescriptor
import com.quasar.dorm.ObjectManager
import com.quasar.dorm.persistence.entity.AttributeEntity
import com.quasar.dorm.persistence.entity.EntityEntity
import jakarta.persistence.criteria.*

class From(val objectDescriptor: ObjectDescriptor) : ObjectPath(null) {
    override fun get(property: String) : ObjectPath {
        return PropertyPath(this, objectDescriptor.property(property))
    }

    override fun path(root: Root<AttributeEntity>) : Path<Any> {
        return root as Path<Any>
    }

    override fun <T> expression(root: Root<AttributeEntity>): Path<T> {
        return root as Path<T>
    }

    override fun type() : Class<Any> {
        return ObjectDescriptor::class as Class<Any>
    }
}

abstract class BooleanExpression :  ObjectExpression()
abstract class ComparisonExpression(val path: ObjectPath) : BooleanExpression() {

    abstract fun where(executor: QueryExecutor<Any>, builder: CriteriaBuilder, attribute: Root<AttributeEntity>) : Predicate

    override fun createWhere(executor: QueryExecutor<Any>, builder: CriteriaBuilder, query: CriteriaQuery<Any>, from: Root<Any>) : Predicate {
        val subQuery = query.subquery(Int::class.java)
        val attribute = subQuery.from(AttributeEntity::class.java) // type: string, entity: Int

        val attributeName = path.attributeName()

        // id is the id of the entity...
        if ( attributeName == "id")
            subQuery
                .select(attribute.get<EntityEntity>("entity").get<Int>("id"))
                .distinct(true)
                .where(this.where(executor, builder, attribute))
        else
            subQuery
                .select(attribute.get<EntityEntity>("entity").get<Int>("id"))
                .distinct(true)
                .where(
                    // object type

                    builder.equal(attribute.get<String>("type"), executor.query.root!!.objectDescriptor.name),

                    // the attribute

                    builder.equal(attribute.get<String>("attribute"), attributeName),

                    // where

                    this.where(executor, builder, attribute)
                )

        val attr = if ( from.javaType == EntityEntity::class.java ) "id" else "entity"

        return builder.`in`(from.get<EntityEntity>("entity").get<Int>("id")).value(subQuery) // TODO RELATION
    }
}

class And(vararg expr: BooleanExpression) : BooleanExpression() {
    val expressions = expr
    override fun createWhere(executor: QueryExecutor<Any>, builder: CriteriaBuilder, query: CriteriaQuery<Any>, from: Root<Any>) : Predicate {
        return builder.and(*expressions.map { expr -> expr.createWhere(executor, builder, query, from) }.toTypedArray())
    }
}

class Or(vararg expr: BooleanExpression) : BooleanExpression() {
    val expressions = expr
    override fun createWhere(executor: QueryExecutor<Any>, builder: CriteriaBuilder, query: CriteriaQuery<Any>, from: Root<Any>) : Predicate {
        return builder.or(*expressions.map { expr -> expr.createWhere(executor, builder, query, from) }.toTypedArray())
    }
}

class Eq(path: ObjectPath, val value: Value) : ComparisonExpression(path) {
    // override

    override fun where(executor: QueryExecutor<Any>, builder: CriteriaBuilder, attribute: Root<AttributeEntity>) : Predicate {
        return builder.equal(path.expression<Any>(attribute), value.resolve(executor, Any::class.java))
    }
}

class Neq(path: ObjectPath, val value: Value) : ComparisonExpression(path) {
    // override

    override fun where(executor: QueryExecutor<Any>, builder: CriteriaBuilder, attribute: Root<AttributeEntity>) : Predicate {
        return builder.notEqual(path.expression<Any>(attribute), value.resolve(executor, Any::class.java))
    }
}

class Lt(path: ObjectPath, val value: Value) : ComparisonExpression(path) {
    // override

    override fun where(executor: QueryExecutor<Any>, builder: CriteriaBuilder, attribute: Root<AttributeEntity>) : Predicate {
        return builder.lt(path.expression(attribute), value.resolve(executor, Number::class.java))
    }
}

class Le(path: ObjectPath, val value: Value) : ComparisonExpression(path) {
    // override

    override fun where(executor: QueryExecutor<Any>, builder: CriteriaBuilder, attribute: Root<AttributeEntity>) : Predicate {
        return builder.le(path.expression(attribute), value.resolve(executor, Number::class.java))
    }
}

class Gt(path: ObjectPath, val value: Value) : ComparisonExpression(path) {
    // override

    override fun where(executor: QueryExecutor<Any>, builder: CriteriaBuilder, attribute: Root<AttributeEntity>) : Predicate {
        return builder.gt(path.expression(attribute), value.resolve(executor, Number::class.java))
    }
}

class Ge(path: ObjectPath, val value: Value) : ComparisonExpression(path) {
    // override

    override fun where(executor: QueryExecutor<Any>, builder: CriteriaBuilder, attribute: Root<AttributeEntity>) : Predicate {
        return builder.ge(path.expression(attribute), value.resolve(executor, Number::class.java))
    }
}

class QueryExecutor<T : Any>(val query: Query<T>, val queryManager: QueryManager) {
    // instance data

    private val parameters = HashMap<String,Any>()

    // public

    fun set(name: String, value: Any) : QueryExecutor<T> {
        val parameter = query.parameter.find { param-> param.name == name }
        if ( parameter != null) {
            parameters[name] = value
        }
        else throw Error("unknown parameter ${name}")

        return this
    }

    // parameter

    fun resolveParameter(name: String) : Any {
        val result = parameters[name]
        if ( result != null)
            return result
        else
            throw Error("unresolved parameter ${name}");
    }

    // execute

    fun execute() : QueryResult<T> {
        return queryManager.execute(query, this)
    }
}

class Query<T : Any>(val resultType: Class<T>, val queryManager: QueryManager, val objectManager: ObjectManager) {
    // instance data

    var root: From? = null
    var where : ObjectExpression? = null
    var projection : Array<out ObjectPath>? = null
    val parameter : MutableList<Parameter> = ArrayList()

    // fluent

    fun from(root: From) : Query<T> {
        this.root = root

        return this
    }

    fun where(expression: ObjectExpression) : Query<T> {
        this.where = expression

        return this
    }

    fun select(vararg path: ObjectPath) : Query<T> {
        if (path.size == 1 && path[0] is From)
            this.root =  path[0] as From
        else
            projection = path

        return this
    }

    // parameter

    fun parameter(name: String) : Parameter {
        val param = Parameter(name)

        this.parameter.add(param)

        return param
    }

    // main execute

    fun executor() : QueryExecutor<T> {
        return QueryExecutor<T>(this, queryManager)
    }

    fun execute() : QueryResult<T> {
        return QueryExecutor(this, queryManager).execute()
    }
}

// global functions

// boolean expressions

fun and(vararg expressions: BooleanExpression) : ObjectExpression {
    return And(*expressions)
}

fun or(vararg expressions: BooleanExpression) : ObjectExpression {
    return Or(*expressions)
}

// arithmetic expressions

fun eq(path: ObjectPath, value: Any) : BooleanExpression {
    return Eq(path, if ( value is Value) value else Constant(value))
}

fun ne(path: ObjectPath, value: Any) : ObjectExpression {
    return Neq(path, if ( value is Value) value else Constant(value))
}

fun lt(path: ObjectPath, value: Any) : ObjectExpression {
    return Lt(path, if ( value is Value) value else Constant(value))
}

fun le(path: ObjectPath, value: Any) : ObjectExpression {
    return Le(path, if ( value is Value) value else Constant(value))
}

fun gt(path: ObjectPath, value: Any) : ObjectExpression {
    return Gt(path, if ( value is Value) value else Constant(value))
}

fun ge(path: ObjectPath, value: Any) : ObjectExpression {
    return Ge(path, if ( value is Value) value else Constant(value))
}