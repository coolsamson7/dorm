package com.example.dorm.query

import com.example.dorm.model.ObjectDescriptor
import com.example.dorm.ObjectManager
import com.example.dorm.persistence.entity.AttributeEntity
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
}

abstract class BooleanExpression :  ObjectExpression()
abstract class ComparisonExpression(val path: ObjectPath) : BooleanExpression() {

    abstract fun where(executor: QueryExecutor<Any>, builder: CriteriaBuilder, attribute: Root<AttributeEntity>) : Predicate

    override fun createWhere(executor: QueryExecutor<Any>, builder: CriteriaBuilder, query: CriteriaQuery<Any>, from: Root<Any>) : Predicate {
        val subQuery = query.subquery(Int::class.java)
        val attribute = subQuery.from(AttributeEntity::class.java) // type: string, entity: Int

        val attributeName = path.attributeName()

        if ( attributeName == "id")
            subQuery
                .select(attribute.get("entity"))
                .distinct(true)
                .where(this.where(executor, builder, attribute)) //7 TODO ID
        else
            subQuery
                .select(attribute.get("entity"))
                .distinct(true)
                .where(
                    // object type

                    builder.equal(attribute.get<String>("type"), executor.query.root!!.objectDescriptor.name),

                    // the attribute

                    builder.equal(attribute.get<String>("attribute"), attributeName),

                    // where

                    this.where(executor, builder, attribute)
                )

        return builder.`in`(from.get<Int>("entity")).value(subQuery)
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

class Eq(path: ObjectPath, val value: Value<out Any>) : ComparisonExpression(path) {
    // override

    override fun where(executor: QueryExecutor<Any>, builder: CriteriaBuilder, attribute: Root<AttributeEntity>) : Predicate {
        return builder.equal(path.expression<Any>(attribute), value.resolve(executor))
    }
}

class Neq(path: ObjectPath, val value: Value<out Any>) : ComparisonExpression(path) {
    // override

    override fun where(executor: QueryExecutor<Any>, builder: CriteriaBuilder, attribute: Root<AttributeEntity>) : Predicate {
        return builder.notEqual(path.expression<Any>(attribute), value.resolve(executor))
    }
}

class Lt(path: ObjectPath, val value: Value<out Any>) : ComparisonExpression(path) {
    // override

    override fun where(executor: QueryExecutor<Any>, builder: CriteriaBuilder, attribute: Root<AttributeEntity>) : Predicate {
        return builder.lt(path.expression(attribute), value.resolve(executor) as Number)
    }
}

class Le(path: ObjectPath, val value: Value<out Any>) : ComparisonExpression(path) {
    // override

    override fun where(executor: QueryExecutor<Any>, builder: CriteriaBuilder, attribute: Root<AttributeEntity>) : Predicate {
        return builder.le(path.expression(attribute), value.resolve(executor) as Number)
    }
}

class Gt(path: ObjectPath, val value: Value<out Any>) : ComparisonExpression(path) {
    // override

    override fun where(executor: QueryExecutor<Any>, builder: CriteriaBuilder, attribute: Root<AttributeEntity>) : Predicate {
        return builder.gt(path.expression(attribute), value.resolve(executor) as Number) // TODO
    }
}

class Ge(path: ObjectPath, val value: Value<out Any>) : ComparisonExpression(path) {
    // override

    override fun where(executor: QueryExecutor<Any>, builder: CriteriaBuilder, attribute: Root<AttributeEntity>) : Predicate {
        return builder.ge(path.expression(attribute), value.resolve(executor) as Number)
    }
}

class QueryExecutor<T : Any>(val query: Query<T>, val queryManager: QueryManager) {
    // instance data

    private val parameters = HashMap<String,Any>()

    // public

    fun set(name: String, value: Any) : QueryExecutor<T> {
        val parameter = query.parameter.find { param-> param.name == name }
        if ( parameter != null) {
            parameter.checkType(value)

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
    val parameter : MutableList<Parameter<Any>> = ArrayList()

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

    fun parameter(name: String) : Parameter<Any> {
        val param = Parameter(name, Any::class.java)

        this.parameter.add(param)

        return param
    }

    // boolean expressions

    fun and(vararg expressions: BooleanExpression) : ObjectExpression {
        return And(*expressions)
    }

    fun or(vararg expressions: BooleanExpression) : ObjectExpression {
        return Or(*expressions)
    }

    // arithmetic expressions

    fun eq(path: ObjectPath, value: Any) : BooleanExpression {
        return Eq(path, if ( value is Value<*>) value else Constant(value))
    }

    fun neq(path: ObjectPath, value: Any) : ObjectExpression {
        return Neq(path, if ( value is Value<*>) value else Constant(value))
    }

    fun lt(path: ObjectPath, value: Any) : ObjectExpression {
        return Lt(path, if ( value is Value<*>) value else Constant(value))
    }

    fun le(path: ObjectPath, value: Any) : ObjectExpression {
        return Le(path, if ( value is Value<*>) value else Constant(value))
    }

    fun gt(path: ObjectPath, value: Any) : ObjectExpression {
        return Gt(path, if ( value is Value<*>) value else Constant(value))
    }

    fun ge(path: ObjectPath, value: Any) : ObjectExpression {
        return Ge(path, if ( value is Value<*>) value else Constant(value))
    }

    // main execute

    fun executor() : QueryExecutor<T> {
        return QueryExecutor<T>(this, queryManager)
    }

    fun execute() : QueryResult<T> {
        return QueryExecutor(this, queryManager).execute()
    }
}