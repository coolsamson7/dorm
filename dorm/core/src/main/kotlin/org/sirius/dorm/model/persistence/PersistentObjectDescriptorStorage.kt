package org.sirius.dorm.model.persistence
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.fasterxml.jackson.databind.ObjectMapper
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.ObjectDescriptorStorage
import org.sirius.dorm.persistence.entity.EntitySchemaEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.sirius.dorm.persistence.entity.EntityStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PersistentObjectDescriptorStorage : ObjectDescriptorStorage {
    // instance data

    @PersistenceContext
    private lateinit var entityManager: EntityManager


    @Autowired
    private lateinit var objectMapper: ObjectMapper


    // implement ObjectDescriptorStorage

    override fun store(objectDescriptor: ObjectDescriptor) {
        val json = objectMapper.writeValueAsString(objectDescriptor)
        entityManager.persist(EntitySchemaEntity(objectDescriptor.name, 0, EntityStatus.NEW, json))
    }

    override fun findByName(name: String) : ObjectDescriptor? {
        val entity = entityManager.find(EntitySchemaEntity::class.java, name)

        return if ( entity !== null) {
            val descriptor = objectMapper.readValue(entity.json, ObjectDescriptor::class.java)

            return descriptor
        }
        else
            null
    }
}