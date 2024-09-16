package com.example.dorm

import com.example.dorm.model.ObjectDescriptor
import com.example.dorm.type.base.int
import com.example.dorm.type.base.string
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.event.annotation.BeforeTestClass

@Configuration()
@Import(DORMConfiguration::class)
class JPAConfiguration {
}


@SpringBootTest(classes = [JPAConfiguration::class])
internal class JPATest {
    @Autowired
    lateinit var manager : ObjectManager

    var personDescriptor : ObjectDescriptor? = null

    @BeforeEach
    fun setupSchema() {
        if ( personDescriptor == null) {
            val stringType = string().length(100)
            val intType = int()

            personDescriptor = manager.type("person")
                .attribute("name", stringType)
                .attribute("age", intType)
                .attribute("v1", stringType)
                .attribute("v2", stringType)
                .attribute("v3", stringType)
                .register()

            /* create test object

            manager.begin()
            try {
                val person1 = manager.create(personDescriptor!!)

                person1["name"] = "Andi"
                person1["age"] = 58
                person1["v1"] = "v1"
                person1["v2"] = "v2"
                person1["v3"] = "v3"

                val person2 = manager.create(personDescriptor!!)

                person2["name"] = "Ernst"
                person2["age"] = 59
                person2["v1"] = "v1"
                person2["v2"] = "v2"
                person2["v3"] = "v3"
            }
            finally {
                manager.commit()
            }*/
        }
    }
    /*@Test
    fun testProjection() {
        val queryManager = manager.queryManager()

        val query = manager.query<Any>("SELECT p.age, p.name FROM person AS p WHERE p.age = :age")// TODO >

        val tupleResult = query.executor()
             .set("age", 59)
            .execute()
            .getResultList()

        assertEquals(1, tupleResult.size)
    }*/

    @Test
    fun test() {
        // tx & write

        manager.begin()
        try {
            val person1 = manager.create(personDescriptor!!)

            person1["name"] = "Andi"
            person1["age"] = 58
            person1["v1"] = "v1"
            person1["v2"] = "v2"
            person1["v3"] = "v3"

            val person2 = manager.create(personDescriptor!!)

            person2["name"] = "Ernst"
            person2["age"] = 59
            person2["v1"] = "v1"
            person2["v2"] = "v2"
            person2["v3"] = "v3"
        }
        finally {
            manager.commit()
        }

        // update

        val queryManager = manager.queryManager()

        manager.begin()
        try {
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)

            val queryResult = query.execute().getResultList()

            val p1 = queryResult[0]

            p1["age"] = 100
        }
        finally {
            manager.commit()
        }

        // reread updated object

        manager.begin()
        try {
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager.create() // object query
            query
                .select(person)
                .from(person)
                .where(query.eq(person.get("age"), 100))

            val queryResult = query.execute().getResultList()

            assertEquals(1, queryResult.size)

            val p1 = queryResult[0]

            assertEquals(100, p1["age"])
        }
        finally {
            manager.commit()
        }

        // parameter

        manager.begin()
        try {
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager.create() // object query
            query
                .select(person)
                .from(person)
                .where(query.eq(person.get("age"), query.parameter("age")))

            val queryResult = query.executor()
                .set("age", 59)
                .execute().getResultList()

            assertEquals(1, queryResult.size)

            val p1 = queryResult[0]

            assertEquals(59, p1["age"])
        }
        finally {
            manager.commit()
        }

        // query

        manager.begin()
        try {
            val person = queryManager.from(personDescriptor!!)

            // hql

            var query = manager.query<DataObject>("SELECT p FROM person AS p")

            query = manager.query<DataObject>("SELECT p FROM person AS p WHERE p.age = 100")

            var queryResult = query.execute().getResultList()

            assertEquals(1, queryResult.size)

            // hql with parameter

            query = manager.query<DataObject>("SELECT p FROM person AS p WHERE p.age = :age")

            queryResult = query.executor()
                .set("age", 100)
                .execute()
                .getResultList()

            assertEquals(1, queryResult.size)

            // hql with and

            query = manager.query<DataObject>("SELECT p FROM person AS p WHERE p.age = :age AND p.name = :name")

            queryResult = query.executor()
                .set("age", 100)
                .set("name", "Andi")
                .execute()
                .getResultList()

            assertEquals(1, queryResult.size)

            // tuple

            query = manager.query("SELECT p.age, p.name FROM person AS p WHERE p.age = :age")

            val tupleResult = query.executor()
                .set("age", 100)
                .execute()
                .getResultList()

            assertEquals(1, tupleResult.size)

            // no where

            query = queryManager.create().from(person) // object query
            queryResult = query.execute().getResultList()

            assertEquals(2, queryResult.size)

            // with where

            query = queryManager
                .create() // object query
                .select(person)
                .from(person)
                .where(query.eq(person.get("age"), 59))

            queryResult = query.execute().getResultList()

            assertEquals(1, queryResult.size)

            assertEquals("Ernst", queryResult[0]["name"])
            assertEquals(59, queryResult[0]["age"])

            // tuple without where

            var tupleQuery = queryManager
                .create(Array<Any>::class.java) // object query
                .select(person.get("age"), person.get("name"))
                .from(person)

            var tupleQueryResult = tupleQuery.execute().getResultList()

            assertEquals(2, tupleQueryResult.size)

            // tuple with where

            tupleQuery = queryManager
                .create(Array<Any>::class.java) // object query
                .select(person.get("age"))
                .from(person)
                .where(query.eq(person.get("age"), 59))

            tupleQueryResult = tupleQuery.execute().getResultList()

            assertEquals(1, tupleQueryResult.size)
            assertEquals(59, tupleQueryResult[0][0])
        }
        finally {
            manager.commit()
        }

        // delete

        manager.begin()
        try {
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager.create().from(person) // object query
            val queryResult = query.execute().getResultList()

            for ( result in queryResult)
                manager.delete(result)
        }
        finally {
            manager.commit()
        }
    }
}