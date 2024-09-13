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

    override fun expression(root: Root<AttributeEntity>): Path<Any> {
        return root as Path<Any> // TODO
    }
}

class Eq(path: ObjectPath, val value: Value<out Any>) : ObjectExpression(path) {
    // override

    override fun createWhere(executor: QueryExecutor<Any>, builder: CriteriaBuilder, from: Root<AttributeEntity>) : Predicate {
        return builder.equal(path.expression(from), value.resolve(executor))
    }
}

class Neq(path: ObjectPath, val value: Value<out Any>) : ObjectExpression(path) {
    // override

    override fun createWhere(executor: QueryExecutor<Any>, builder: CriteriaBuilder, from: Root<AttributeEntity>) : Predicate {
        return builder.notEqual(path.expression(from), value.resolve(executor))
    }
}

class Lt(path: ObjectPath, val value: Value<out Any>) : ObjectExpression(path) {
    // override

    override fun createWhere(executor: QueryExecutor<Any>, builder: CriteriaBuilder, from: Root<AttributeEntity>) : Predicate {
        return builder.lt(path.expression(from) as Expression<Number>, value.resolve(executor) as Number)
    }
}

class Le(path: ObjectPath, val value: Value<out Any>) : ObjectExpression(path) {
    // override

    override fun createWhere(executor: QueryExecutor<Any>, builder: CriteriaBuilder, from: Root<AttributeEntity>) : Predicate {
        return builder.le(path.expression(from) as Expression<Number>, value.resolve(executor) as Number) // TODO??
    }
}

class Gt(path: ObjectPath, val value: Value<out Any>) : ObjectExpression(path) {
    // override

    override fun createWhere(executor: QueryExecutor<Any>, builder: CriteriaBuilder, from: Root<AttributeEntity>) : Predicate {
        return builder.gt(path.expression(from) as Expression<Number>, value.resolve(executor) as Number)
    }
}

class Ge(path: ObjectPath, val value: Value<out Any>) : ObjectExpression(path) {
    // override

    override fun createWhere(executor: QueryExecutor<Any>, builder: CriteriaBuilder, from: Root<AttributeEntity>): Predicate {
        return builder.ge(path.expression(from) as Expression<Number>, value.resolve(executor) as Number)
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
        else throw Error("unknown paramater ${name}")

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

    // expressions

    fun eq(path: ObjectPath, value: Any) : ObjectExpression {
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