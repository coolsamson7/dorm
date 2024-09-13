package com.example.dorm.query.parser
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.example.dorm.DataObject
import com.example.dorm.model.ObjectDescriptor
import com.example.dorm.query.*
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.TokenStream

class SelectPath {
    val path = ArrayList<String>()

    fun add(leg: String) {
        path.add(leg)
    }

    fun resolve(parser: AbstractQueryParser) : ObjectPath {
        var result : ObjectPath = parser.fetchSchema(path[0])

        for (i in 1..path.size-1)
            result = result.get(path[i])

        return result
    }
}
abstract class AbstractQueryParser(input: TokenStream) : Parser(input) {
    // instance data

    lateinit var queryManager: QueryManager
    var query: Query<DataObject>? = null
    protected var from: From? = null

    protected val variables = HashMap<String,From>()

    // public

    fun setup(queryManager: QueryManager) {
        this.queryManager = queryManager
    }

    // protected

    protected fun condition(selectPath: SelectPath, operator: String, value: Value<Any>) : ObjectExpression {
        val path = selectPath.resolve(this)

        return when (operator) {
            "=" -> query!!.eq(path, value)
            "<>" -> query!!.neq(path, value)
            "<" -> query!!.lt(path, value as Number)
            ">" -> query!!.gt(path, value as Number)
            "<=" -> query!!.le(path, value as Number)
            ">=" -> query!!.ge(path, value as Number) // TODO
            else -> {
                throw Error("unsupported operator ${operator}")
            }
        }
    }

    protected fun rememberSchema(descriptor: String, name: String) : From {
        val from = queryManager.from(queryManager.objectManager.get(descriptor))

        variables[name] = from

        return from
    }

    fun fetchSchema(name: String) : From {
        return variables[name]!!
    }

    protected fun select(query: Query<DataObject>, select: List<SelectPath>) {
        query.select(*select.map { s -> s.resolve(this) }.toTypedArray())
    }

    protected fun from(from: String) {
        this.from = queryManager.from(queryManager.objectManager.get(from))
    }
}