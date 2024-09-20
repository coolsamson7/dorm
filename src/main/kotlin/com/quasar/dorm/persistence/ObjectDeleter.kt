package com.quasar.dorm.persistence
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.`object`.DataObject
import com.quasar.dorm.persistence.entity.AttributeEntity
import com.quasar.dorm.persistence.entity.EntityEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.Query
import jakarta.persistence.criteria.ParameterExpression

class ObjectDeleter(entityManager: EntityManager) {
    // instance data

    val attributeId : ParameterExpression<Int>
    val entityId : ParameterExpression<Int>
    val deleteAttribute : Query
    val deleteEntity : Query

    // init

    init {
        val builder = entityManager.criteriaBuilder

        // delete attributes

        attributeId = builder.parameter<Int>(Int::class.java)

        val deleteAttributeQuery = builder.createCriteriaDelete(AttributeEntity::class.java)
        val from = deleteAttributeQuery.from(AttributeEntity::class.java)

        deleteAttributeQuery.where(builder.equal(from.get<Int>("entity"), attributeId))

        deleteAttribute = entityManager.createQuery(deleteAttributeQuery)

        // delete entity

        entityId = builder.parameter(Int::class.java)

        val criteriaQueryEntity = builder.createCriteriaDelete(EntityEntity::class.java)
        val fromEntity = criteriaQueryEntity.from(EntityEntity::class.java)
        criteriaQueryEntity.where(builder.equal(fromEntity.get<Int>("id"), entityId))

        deleteEntity = entityManager.createQuery(criteriaQueryEntity)
    }

    // public

    fun delete(obj: DataObject) {
        deleteAttribute.setParameter(attributeId, obj.id).executeUpdate()
        deleteEntity.setParameter(entityId, obj.id).executeUpdate()
    }
}