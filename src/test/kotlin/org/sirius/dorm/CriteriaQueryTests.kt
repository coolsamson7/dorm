package org.sirius.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.aspectj.lang.annotation.Before
import org.h2.tools.Server
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.sirius.dorm.`object`.MultiValuedRelation
import org.sirius.dorm.query.*
import java.sql.SQLException
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CriteriaQueryTests : AbstractTest() {
    //@BeforeAll
    fun setup() {
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082")
            .start();
    }

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
    fun testJoin() {
        objectManager.begin()
        try {
            val person = objectManager.create(personDescriptor!!)

            person["name"] = "Andi"

            val child = objectManager.create(personDescriptor!!)

            child["name"] = "Nika"

            person.relation<MultiValuedRelation>("children").add(child)

        }
        finally {
            objectManager.commit()
        }

        printTables()

        withTransaction { ->
            val queryManager = objectManager.queryManager()
            val person = queryManager.from(personDescriptor!!)
            val children = person.join("children")

            // no where

            val query = queryManager
                .create(Array<Any>::class.java)
                .select(person)
                .from(person)
                .where(eq(children.get("name"), "Nika"))

            val result = query.execute().getResultList()

            assertEquals(1, result.size)
        }

    }

    @Test
    fun testReadId() {
        createPerson("Andi", 58)

        withTransaction { ->
            val queryManager = objectManager.queryManager()
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)

            val result = query.execute().getResultList()

            assertEquals(1, result.size)

            val id = result[0].id

            // query by id

            // no where

            val idQuery = queryManager
                .create()
                .select(person)
                .from(person)
                .where(eq(person.get("id"), id))

            val list = idQuery.execute().getResultList()

            assertEquals(1, list.size)


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
                .where(eq(person.get("age"), 58))

            val result = query.execute().getResultList()

            assertEquals(1, result.size)
            assertEquals(58, result[0]["age"])
        }
    }

    @Test
    fun testFlush() {
        createPerson("Andi", 58)

        withTransaction {
            val queryManager = objectManager.queryManager()

            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)

            query.where(eq(person.get("age"), query.parameter("age")))

            var result = query.executor()
                .set("age", 58)
                .execute()
                .getResultList()

            assertEquals(1, result.size)

            val andi = result[0]

            // now change

            andi["age"] = 59 // ouch!!!!

            result = query.executor()
                .set("age", 58)
                .execute()
                .getResultList()

            assertEquals(0, result.size)
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

            query.where(eq(person.get("age"), query.parameter("age")))

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
                .where(and(
                    eq(person.get("name"), "Andi"),
                    eq(person.get("age"), 58)
                ))

            val result = query.executor()
                .execute()
                .getResultList()

            assertEquals(1, result.size)
            assertEquals(58, result[0]["age"])
        }
    }
}