package com.quasar.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.`object`.DataObject
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.hibernate.Session
import org.hibernate.stat.Statistics
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class QLQueryTests : AbstractTest() {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    lateinit var statistics : Statistics;

    @BeforeEach
    fun clearStats() {
        val session = entityManager.unwrap(Session::class.java)

        statistics = session.getSessionFactory().getStatistics()
    }

    @AfterEach
    fun printStats() {
        for ( query in statistics.queries)
            println(query)
    }

    @Test
    fun testReadAll() {
        createPerson("Andi", 58)

        withTransaction { ->
            val query = objectManager.query<DataObject>("SELECT p FROM person AS p")

            val queryResult = query.execute().getResultList()

            Assertions.assertEquals(1, queryResult.size)
        }
    }

    @Test
    fun testParameter() {
        createPerson("Andi", 58)

        withTransaction { ->
            val query = objectManager.query<DataObject>("SELECT p FROM person AS p WHERE p.age = :age")

            val queryResult = query.executor()
                .set("age", 58)
                .execute()
                .getResultList()

            Assertions.assertEquals(1, queryResult.size)
        }
    }

    @Test
    fun testProjection() {
        createPerson("Andi", 58)

        withTransaction { ->
            val query = objectManager.query<Array<Any>>("SELECT p.age, p.name FROM person AS p WHERE p.age = :age")

            val queryResult = query.executor()
                .set("age", 58)
                .execute()
                .getResultList()

            Assertions.assertEquals(1, queryResult.size)
            Assertions.assertEquals(2, queryResult[0].size)
        }
    }

    @Test
    fun testWhere() {
        createPerson("Andi", 58)

        withTransaction { ->
            val query = objectManager.query<DataObject>("SELECT p FROM person AS p WHERE p.age = 58")

            val queryResult = query.execute().getResultList()

            Assertions.assertEquals(1, queryResult.size)
        }
    }

    @Test
    fun testLogicalOperator() {
        createPerson("Andi", 58)

        withTransaction { ->
            val query = objectManager.query<DataObject>("SELECT p FROM person AS p WHERE p.age = :age AND p.name = :name")

            val queryResult = query.executor()
                .set("name", "Andi")
                .set("age", 58)
                .execute()
                .getResultList()

            Assertions.assertEquals(1, queryResult.size)
        }
    }
}