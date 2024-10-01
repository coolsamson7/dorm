package org.sirius.dorm.query
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import jakarta.persistence.criteria.*
import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.RelationDescriptor
import org.sirius.dorm.persistence.entity.EntityEntity
import org.sirius.dorm.persistence.entity.PropertyEntity

abstract class AbstractFrom(parent : ObjectPath? = null) : ObjectPath(parent) {
    /*override fun get(property: String) : ObjectPath {
    //    return PropertyPath(this, objectDescriptor.property(property))
    //}

    override fun <T> expression(root: Root<PropertyEntity>): Path<T> {
        return root as Path<T>
    }*/

    override fun type(): Class<Any> {
        return ObjectDescriptor::class as Class<Any>
    }
}

class FromRoot(val objectDescriptor: ObjectDescriptor) : AbstractFrom(null) {
    // fluent

    override fun get(property: String): ObjectPath {
        return PropertyPath(this, objectDescriptor.property(property))
    }

    // internal

    override fun <T> expression(root: Root<PropertyEntity>, builder: CriteriaBuilder, query: AbstractQuery<*>): Path<T> {
        return root as Path<T>
    }

    override fun type(): Class<Any> {
        return ObjectDescriptor::class as Class<Any>
    }

    // here?

    fun join(property: String): JoinFrom {
        return JoinFrom(this, property)
    }
}

class JoinFrom(root: FromRoot, val property: String) : AbstractFrom(root) {
    // instance data

    val relationship = root.objectDescriptor.property(property) as RelationDescriptor

    // override

    override fun get(property: String): ObjectPath {
        return PropertyPath(this, relationship.targetDescriptor!!.property(property))
    }

    override fun <T> expression(root: Root<PropertyEntity>, builder: CriteriaBuilder, query: AbstractQuery<*>): Path<T> {
        if ( resolvedPath == null) {
            val parentPath = this.parent?.expression<T>(root, builder, query)!! as From<*, *>

            resolvedPath = parentPath
                .join<PropertyEntity, PropertyEntity>("targets")
                .on(builder.equal(parentPath.get<Long>("attribute"), property))
                // ugly, since we already know the entity id TODO
                .join<PropertyEntity, PropertyEntity>("entity")
                .join<PropertyEntity, PropertyEntity>("properties")
        }

        return resolvedPath as Path<T>
    }
}

abstract class BooleanExpression : ObjectExpression()
abstract class ComparisonExpression(val path: ObjectPath) : BooleanExpression() {

    abstract fun where(
        executor: QueryExecutor<Any>,
        builder: CriteriaBuilder,
        query: AbstractQuery<*>,
        property: Root<PropertyEntity>
    ): Predicate

    fun constructWhere(builder: CriteriaBuilder, query: AbstractQuery<*>, property: Root<PropertyEntity>, predicate: Predicate) : Predicate {
        return if (path.attributeName() != "id")
            builder.and(
                builder.equal(path.parent!!.expression<Any>(property, builder, query).get<String>("attribute"), path.attributeName()),
                predicate
            )
        else predicate
    }

    override fun createWhere(
        executor: QueryExecutor<Any>,
        builder: CriteriaBuilder,
        query: CriteriaQuery<Any>,
        from: Root<Any>
    ): Predicate {
        val subQuery = query.subquery(Int::class.java)
        val property = subQuery.from(PropertyEntity::class.java)

        val attributeName = path.attributeName()

        // id is the id of the entity...

        if (attributeName == "id")
            subQuery
                .select(property.get<EntityEntity>("entity").get("id"))
                .distinct(true)
                .where(this.where(executor, builder, subQuery, property))
        else {
            subQuery
                .select(property.get<EntityEntity>("entity").get("id"))
                .distinct(true)
                .where(
                    // object type

                    /* TODO das muss woanders hin builder.equal(
                        property.get<String>("type"),
                        (executor.query.root as FromRoot).objectDescriptor.name
                    ),*/

                    // the attribute

                    //already added in where(...)builder.equal(property.get<String>("attribute"), attributeName),

                    // where

                    this.where(executor, builder, subQuery, property)
                )
        }

        return builder.`in`(from.get<EntityEntity>("entity").get<Int>("id")).value(subQuery)
    }
}

class And(vararg expr: BooleanExpression) : BooleanExpression() {
    val expressions = expr
    override fun createWhere(
        executor: QueryExecutor<Any>,
        builder: CriteriaBuilder,
        query: CriteriaQuery<Any>,
        from: Root<Any>
    ): Predicate {
        return builder.and(*expressions.map { expr -> expr.createWhere(executor, builder, query, from) }.toTypedArray())
    }
}

class Or(vararg expr: BooleanExpression) : BooleanExpression() {
    val expressions = expr
    override fun createWhere(
        executor: QueryExecutor<Any>,
        builder: CriteriaBuilder,
        query: CriteriaQuery<Any>,
        from: Root<Any>
    ): Predicate {
        return builder.or(*expressions.map { expr -> expr.createWhere(executor, builder, query, from) }.toTypedArray())
    }
}

class Eq(path: ObjectPath, val value: Value) : ComparisonExpression(path) {
    // override

    override fun where(
        executor: QueryExecutor<Any>,
        builder: CriteriaBuilder,
        query: AbstractQuery<*>,
        property: Root<PropertyEntity>
    ): Predicate {
        return constructWhere(
            builder, query, property,
            builder.equal(path.expression<Any>(property, builder, query), value.resolve(executor, Any::class.java))
        )
    }
}

class Neq(path: ObjectPath, val value: Value) : ComparisonExpression(path) {
    // override

    override fun where(
        executor: QueryExecutor<Any>,
        builder: CriteriaBuilder,
        query: AbstractQuery<*>,
        property: Root<PropertyEntity>
    ): Predicate {
        return constructWhere(
            builder, query, property,
            builder.notEqual(path.expression<Any>(property, builder, query), value.resolve(executor, Any::class.java))
        )
    }
}

class Lt(path: ObjectPath, val value: Value) : ComparisonExpression(path) {
    // override

    override fun where(
        executor: QueryExecutor<Any>,
        builder: CriteriaBuilder,
        query: AbstractQuery<*>,
        property: Root<PropertyEntity>
    ): Predicate {
        return constructWhere(
            builder, query, property, builder.lt(path.expression(property, builder, query), value.resolve(executor, Number::class.java)))
    }
}

class Le(path: ObjectPath, val value: Value) : ComparisonExpression(path) {
    // override

    override fun where(
        executor: QueryExecutor<Any>,
        builder: CriteriaBuilder,
        query: AbstractQuery<*>,
        property: Root<PropertyEntity>
    ): Predicate {
        return constructWhere(
            builder, query, property, builder.le(path.expression(property, builder, query), value.resolve(executor, Number::class.java)))
    }
}

class Gt(path: ObjectPath, val value: Value) : ComparisonExpression(path) {
    // override

    override fun where(
        executor: QueryExecutor<Any>,
        builder: CriteriaBuilder,
        query: AbstractQuery<*>,
        property: Root<PropertyEntity>
    ): Predicate {
        return constructWhere(
            builder, query, property, builder.gt(path.expression(property, builder, query), value.resolve(executor, Number::class.java)))
    }
}

class Ge(path: ObjectPath, val value: Value) : ComparisonExpression(path) {
    // override

    override fun where(
        executor: QueryExecutor<Any>,
        builder: CriteriaBuilder,
        query: AbstractQuery<*>,
        property: Root<PropertyEntity>
    ): Predicate {
        return constructWhere(
            builder, query, property, builder.ge(path.expression(property, builder, query), value.resolve(executor, Number::class.java)))
    }
}

class QueryExecutor<T : Any>(val query: Query<T>, val queryManager: QueryManager) {
    // instance data

    private val parameters = HashMap<String, Any>()

    // public

    fun set(name: String, value: Any): QueryExecutor<T> {
        val parameter = query.parameter.find { param -> param.name == name }
        if (parameter != null) {
            parameters[name] = value
        }
        else throw Error("unknown parameter ${name}")

        return this
    }

    // parameter

    fun resolveParameter(name: String): Any {
        val result = parameters[name]
        if (result != null)
            return result
        else
            throw Error("unresolved parameter ${name}");
    }

    // execute

    fun execute(): QueryResult<T> {
        return queryManager.execute(query, this)
    }
}

class Query<T : Any>(val resultType: Class<T>, val queryManager: QueryManager, val objectManager: ObjectManager) {
    // instance data

    var root: AbstractFrom? = null
    var where: ObjectExpression? = null
    var projection: Array<out ObjectPath>? = null
    val parameter: MutableList<Parameter> = ArrayList()

    // fluent

    fun from(root: AbstractFrom): Query<T> {
        this.root = root

        return this
    }

    fun where(expression: ObjectExpression): Query<T> {
        this.where = expression

        return this
    }

    fun select(vararg path: ObjectPath): Query<T> {
        if (path.size == 1 && path[0] is FromRoot)
            this.root = path[0] as FromRoot
        else
            projection = path

        return this
    }

    // parameter

    fun parameter(name: String): Parameter {
        val param = Parameter(name)

        this.parameter.add(param)

        return param
    }

    // main execute

    fun executor(): QueryExecutor<T> {
        return QueryExecutor<T>(this, queryManager)
    }

    fun execute(): QueryResult<T> {
        return QueryExecutor(this, queryManager).execute()
    }
}

// global functions

// boolean expressions

fun and(vararg expressions: BooleanExpression): ObjectExpression {
    return And(*expressions)
}

fun or(vararg expressions: BooleanExpression): ObjectExpression {
    return Or(*expressions)
}

// arithmetic expressions

fun eq(path: ObjectPath, value: Any): BooleanExpression {
    return Eq(path, if (value is Value) value else Constant(value))
}

fun ne(path: ObjectPath, value: Any): ObjectExpression {
    return Neq(path, if (value is Value) value else Constant(value))
}

fun lt(path: ObjectPath, value: Any): ObjectExpression {
    return Lt(path, if (value is Value) value else Constant(value))
}

fun le(path: ObjectPath, value: Any): ObjectExpression {
    return Le(path, if (value is Value) value else Constant(value))
}

fun gt(path: ObjectPath, value: Any): ObjectExpression {
    return Gt(path, if (value is Value) value else Constant(value))
}

fun ge(path: ObjectPath, value: Any): ObjectExpression {
    return Ge(path, if (value is Value) value else Constant(value))
}