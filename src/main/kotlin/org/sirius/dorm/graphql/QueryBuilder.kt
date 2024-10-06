package org.sirius.dorm.graphql
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.`object`.DataObject
import org.sirius.dorm.query.*

class QueryBuilder(val objectManager: ObjectManager) {
    val queryManager = objectManager.queryManager()

    // public

    fun buildQuery(descriptor: ObjectDescriptor, filter: Any?) : Query<DataObject> {
        val query = queryManager.create()
        val type = queryManager.from(descriptor)

        query
            .select(type)
            .from(type)

        if ( filter !== null)
            query.where(buildExpression(type, filter as Map<String,Any>))

        return query
    }

    // private

    private fun buildExpression(root: FromRoot, filter: Map<String,Any>) : ObjectExpression {
        if ( filter.containsKey("and")) {
            val expressions = filter.get("and") as List<Any>

            return and(*expressions.map { child ->
                buildExpression(
                    root,
                    child as Map<String, Any>
                ) as BooleanExpression
            }.toTypedArray())
        }

        else if ( filter.containsKey("or")) {
            val expressions = filter.get("or") as List<Any>

            return or(*expressions.map { child ->
                buildExpression(
                    root,
                    child as Map<String, Any>
                ) as BooleanExpression
            }.toTypedArray())
        }

        else {
            // must be one of the different

            val key = filter.keys.iterator().next()

            val property = root.objectDescriptor.property(key)

            when ( property.asAttribute().baseType()) {
                Short::class.java -> return buildNumericPredicate(root.get(key), filter.get(key) as Map<String,Any> )
                Int::class.java -> return buildNumericPredicate(root.get(key), filter.get(key) as Map<String,Any>)
                Integer::class.java -> return buildNumericPredicate(root.get(key), filter.get(key) as Map<String,Any>)
                Long::class.java -> return buildNumericPredicate(root.get(key), filter.get(key) as Map<String,Any>)
                Float::class.java -> return buildNumericPredicate(root.get(key), filter.get(key) as Map<String,Any>)
                Double::class.java -> return buildNumericPredicate(root.get(key), filter.get(key) as Map<String,Any>)
                String::class.java -> return buildStringPredicate(root.get(key), filter.get(key) as Map<String,Any>)
                else -> {
                    throw Error("unsupported type ${property.asAttribute().baseType()}")
                }
            }
        }
    }

    private fun buildNumericPredicate(path: ObjectPath, filter: Map<String,Any>): ObjectExpression {
        return when (val predicate = filter.keys.iterator().next()) {
            "lt" -> lt(path, filter.get(predicate)!!)
            "le" -> le(path, filter.get(predicate)!!)
            "gt" -> gt(path, filter.get(predicate)!!)
            "ge" -> ge(path, filter.get(predicate)!!)
            "eq" -> eq(path, filter.get(predicate)!!)
            "ne" -> ne(path, filter.get(predicate)!!)
            else -> {
                throw Error("ouch")
            }
        }
    }
    private fun buildStringPredicate(path: ObjectPath, filter: Map<String,Any>): ObjectExpression {
        return when (val predicate = filter.keys.iterator().next()) {
            "eq" -> eq(path, filter.get(predicate)!!)
            "ne" -> ne(path, filter.get(predicate)!!)
            else -> {
             throw Error("ouch")
            }
        }
    }
}