package com.example.dorm.query

import com.example.dorm.persistence.entity.AttributeEntity
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Root

abstract class ObjectPath( val parent : ObjectPath? = null) {

    abstract fun get(property: String) : ObjectPath

    abstract fun path(root: Root<AttributeEntity>) : Path<Any>

    abstract fun <T>expression(root: Root<AttributeEntity>): Path<T>

    open fun attributeName() : String {
        throw Error("abstract")
    }
}