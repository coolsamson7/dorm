package com.example.dorm.query

import com.example.dorm.persistence.entity.AttributeEntity
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root

abstract class ObjectExpression() {
    abstract fun createWhere(executor: QueryExecutor<Any>, builder: CriteriaBuilder, query: CriteriaQuery<Any>, from: Root<Any>) : Predicate
}