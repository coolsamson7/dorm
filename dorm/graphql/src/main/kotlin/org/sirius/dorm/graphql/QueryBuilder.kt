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
    private val queryManager = objectManager.queryManager()

    // public

    fun buildQuery(descriptor: ObjectDescriptor, filter: Any?) : Query<DataObject> {
        val query = queryManager.query()
        val type = queryManager.from(descriptor)

        query
            .select(type)
            .from(type)

        if ( filter !== null)
            query.where(buildExpression(type, filter as Map<String,Any>))

        return query
    }

    // private

    private fun buildExpression(root: AbstractFrom, filter: Map<String,Any>) : ObjectExpression {
        // and

        if ( filter.containsKey("and")) {
            val expressions = filter.get("and") as List<Any>

            return and(*expressions.map { child ->
                buildExpression(
                    root,
                    child as Map<String, Any>
                ) as BooleanExpression
            }.toTypedArray())
        }

        // from

        else if ( filter.containsKey("or")) {
            val expressions = filter.get("or") as List<Any>

            return or(*expressions.map { child ->
                buildExpression(
                    root,
                    child as Map<String, Any>
                ) as BooleanExpression
            }.toTypedArray())
        }

        // property

        else {
            val key = filter.keys.iterator().next()

            val property = root.descriptor().property(key)

            // either attribute or relation

            if ( property.isAttribute()) {
                when (property.asAttribute().baseType()) {
                    Boolean::class.javaObjectType -> return buildBooleanPredicate(
                        root.get(key),
                        filter.get(key) as Map<String, Any>
                    )

                    Short::class.javaObjectType -> return buildNumericPredicate(
                        root.get(key),
                        filter.get(key) as Map<String, Any>
                    )

                    Int::class.javaObjectType -> return buildNumericPredicate(root.get(key), filter.get(key) as Map<String, Any>)
                    Integer::class.javaObjectType -> return buildNumericPredicate(
                        root.get(key),
                        filter.get(key) as Map<String, Any>
                    )

                    Long::class.javaObjectType -> return buildNumericPredicate(root.get(key), filter.get(key) as Map<String, Any>)
                    Float::class.javaObjectType -> return buildNumericPredicate(
                        root.get(key),
                        filter.get(key) as Map<String, Any>
                    )

                    Double::class.javaObjectType -> return buildNumericPredicate(
                        root.get(key),
                        filter.get(key) as Map<String, Any>
                    )

                    String::class.javaObjectType -> return buildStringPredicate(
                        root.get(key),
                        filter.get(key) as Map<String, Any>
                    )

                    else -> {
                        throw Error("unsupported type ${property.asAttribute().baseType()}")
                    }
                }
            }
            else {
                //if (!property.asRelation().multiplicity.multiValued) {
                    return buildExpression(root.get(property.name) as AbstractFrom, filter.get(key) as Map<String, Any>)
                //}
                //else
                //    throw Error("NYS")
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
            "like" -> like(path, filter.get(predicate)!!)
            else -> {
             throw Error("ouch")
            }
        }
    }

    private fun buildBooleanPredicate(path: ObjectPath, filter: Map<String,Any>): ObjectExpression {
        return when (val predicate = filter.keys.iterator().next()) {
            "eq" -> eq(path, filter.get(predicate)!!)
            "ne" -> ne(path, filter.get(predicate)!!)
            else -> {
                throw Error("ouch")
            }
        }
    }
}