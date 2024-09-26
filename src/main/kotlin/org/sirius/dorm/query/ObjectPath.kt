package org.sirius.dorm.query
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.persistence.entity.PropertyEntity
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Root

abstract class ObjectPath(val parent : ObjectPath? = null) {

    abstract fun get(property: String) : ObjectPath

    abstract fun path(root: Root<PropertyEntity>) : Path<Any>

    abstract fun <T>expression(root: Root<PropertyEntity>): Path<T>

    abstract fun type() : Class<out Any>

    open fun attributeName() : String {
        throw Error("abstract")
    }
}