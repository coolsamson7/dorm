package org.sirius.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.common.tracer.TraceLevel
import org.sirius.common.tracer.Tracer
import org.sirius.common.tracer.trace.ConsoleTrace
import org.sirius.common.type.base.*
import org.sirius.dorm.model.Multiplicity
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.`object`.DataObject
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.hibernate.Session
import org.hibernate.stat.Statistics
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import


@Configuration()
@Import(DORMConfiguration::class)
class TestConfiguration {
}

@SpringBootTest(classes =[TestConfiguration::class])
class AbstractTest {
    @Autowired
    lateinit var objectManager : ObjectManager
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    protected var personDescriptor : ObjectDescriptor? = null

    init {
        Tracer(ConsoleTrace(), "%t{yyyy-MM-dd HH:mm:ss,SSS} %l{-10s} [%p] %m")
            .setTraceLevel("", TraceLevel.OFF)
            .setTraceLevel("com", TraceLevel.LOW)
            .setTraceLevel("com.sirius.dorm", TraceLevel.HIGH)
    }

    lateinit var statistics : Statistics;

    //@BeforeEach
    fun clearStats() {
        val session = entityManager.unwrap(Session::class.java)

        statistics = session.getSessionFactory().getStatistics()
    }

    //@AfterEach
    fun printStats() {
        for ( query in statistics.queries)
            println(query)
    }

    @BeforeEach
    fun setupSchema() {
        withTransaction {
            if ( objectManager.findDescriptor("person") == null) {
                val stringType = string().length(100)
                val intType = int()

                objectManager.type("person")
                    .attribute("name", stringType)
                    .attribute("age", intType)

                    // relations

                    .relation("father", "person", Multiplicity.ZERO_OR_ONE, "children")
                    .relation("children", "person", Multiplicity.ZERO_OR_MANY)

                    .attribute("boolean", boolean())
                    .attribute("string", string())
                    .attribute("short", short())
                    .attribute("int", int())
                    .attribute("long", long())
                    .attribute("float", float())
                    .attribute("double", double())
                    .register()
            }

            personDescriptor = objectManager.getDescriptor("person")
        }
    }

    // delete

    @AfterEach
    fun deletePerson() {
        val queryManager = objectManager.queryManager()
        objectManager.begin()
        try {
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager.create().from(person) // object query
            val queryResult = query.execute().getResultList()

            for (result in queryResult)
                objectManager.delete(result)
        }
        finally {
            objectManager.commit()
        }
    }

    protected fun withTransaction(doIt: () -> Unit) {
        objectManager.begin()
        try {
            doIt()
        }
        finally {
            objectManager.commit()
        }
    }

    protected fun readPersons(): List<DataObject> {
        val queryManager = objectManager.queryManager()
        val person = queryManager.from(personDescriptor!!)

        // no where

        val query = queryManager
            .create()
            .select(person)
            .from(person)

        return query.execute().getResultList()
    }

    protected fun createPerson(name: String, age: Int) {
        objectManager.begin()
        try {
            val person1 = objectManager.create(personDescriptor!!)

            person1["name"] = name
            person1["age"] = age
        }
        finally {
            objectManager.commit()
        }
    }
}