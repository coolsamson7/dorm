package org.sirius.dorm.query
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import jakarta.persistence.criteria.AbstractQuery
import jakarta.persistence.criteria.CriteriaBuilder
import org.sirius.dorm.persistence.entity.PropertyEntity
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Root

abstract class ObjectPath(val parent : ObjectPath? = null) {
    // instance data

    var resolvedPath: Path<*>? = null

    // fluent
    abstract fun get(property: String) : ObjectPath

    // internal

    // SENDER: where creation!
    abstract fun <T>expression(root: Root<PropertyEntity>, builder: CriteriaBuilder, query: AbstractQuery<*>): Path<T>

    abstract fun type() : Class<out Any>

    open fun attributeName() : String {
        throw Error("abstract")
    }
}