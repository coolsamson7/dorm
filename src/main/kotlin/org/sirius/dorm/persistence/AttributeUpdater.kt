package org.sirius.dorm.persistence
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.persistence.entity.AttributeEntity
import org.sirius.dorm.persistence.entity.EntityEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.Query
import jakarta.persistence.criteria.ParameterExpression

class AttributeUpdater<T>(attribute: String, type: Class<T>, entityManager: EntityManager) {
    // instance data

    val entityId : ParameterExpression<Int>
    val attributeId : ParameterExpression<String>
    val value : ParameterExpression<T>
    val updateAttribute : Query

    // init

    init {
        val builder = entityManager.criteriaBuilder

        // delete attributes

        entityId    = builder.parameter(Int::class.java)
        attributeId = builder.parameter(String::class.java)
        value       = builder.parameter(type)

        val updateAttributeQuery = builder.createCriteriaUpdate(AttributeEntity::class.java)
        val from = updateAttributeQuery.from(AttributeEntity::class.java)

        updateAttributeQuery.set(attribute, value);

        updateAttributeQuery.where(builder.and(
            builder.equal(from.get<EntityEntity>("entity").get<Int>("id"), entityId),
            builder.equal(from.get<Int>("attribute"), attributeId)
        ))

        updateAttribute = entityManager.createQuery(updateAttributeQuery)
    }

    // public

    fun update(entity: Int, attribute: String, value: T) {
        updateAttribute
            .setParameter(entityId, entity)
            .setParameter(attributeId, attribute)
            .setParameter(this.value, value)
            .executeUpdate()
    }
}