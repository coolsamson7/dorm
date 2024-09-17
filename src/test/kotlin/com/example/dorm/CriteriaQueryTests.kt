package com.example.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CriteriaQueryTests : AbstractTest() {
    @Test
    fun testReadAll() {
        createPerson("Andi", 58)

        withTransaction { ->
            val queryManager = objectManager.queryManager()
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager
                .create(Array<Any>::class.java)
                .select(person)
                .from(person)

            val result = query.execute().getResultList()

            assertEquals(1, result.size)
        }

    }

    @Test
    fun testProjection() {
        createPerson("Andi", 58)

        withTransaction {
            val queryManager = objectManager.queryManager()
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager
                .create(Array<Any>::class.java)
                .select(person.get("age"))
                .from(person)

            val result = query.execute().getResultList()

            assertEquals(1, result.size)
            assertEquals(58, result[0][0])
        }
    }

    @Test
    fun testWhere() {
        createPerson("Andi", 58)

        withTransaction {
            val queryManager = objectManager.queryManager()
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)

            query.where(query.eq(person.get("age"), 58))

            val result = query.execute().getResultList()

            assertEquals(1, result.size)
            assertEquals(58, result[0]["age"])
        }
    }

    @Test
    fun testParameter() {
        createPerson("Andi", 58)

        withTransaction {
            val queryManager = objectManager.queryManager()
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)

            query.where(query.eq(person.get("age"), query.parameter("age")))

            val result = query.executor()
                .set("age", 58)
                .execute()
                .getResultList()

            assertEquals(1, result.size)
            assertEquals(58, result[0]["age"])
        }
    }

    @Test
    fun testLogicalOperator() {
        createPerson("Andi", 58)

        withTransaction {
            val queryManager = objectManager.queryManager()
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)

            query.where(query.and(
                query.eq(person.get("name"), "Andi"),
                query.eq(person.get("age"), 58)
            ))

            val result = query.executor()
                .execute()
                .getResultList()

            assertEquals(1, result.size)
            assertEquals(58, result[0]["age"])
        }
    }
}