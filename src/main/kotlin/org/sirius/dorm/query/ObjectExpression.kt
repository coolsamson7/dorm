package org.sirius.dorm.query
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root

abstract class ObjectExpression() {
    abstract fun createWhere(executor: QueryExecutor<Any>, builder: CriteriaBuilder, query: CriteriaQuery<Any>, from: Root<Any>) : Predicate
}