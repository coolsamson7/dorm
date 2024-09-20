package com.quasar.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.model.Multiplicity
import com.quasar.dorm.model.ObjectDescriptor
import com.quasar.dorm.type.base.*
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


//@ActiveProfilesTOD("local")
//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes =[TestConfiguration::class])
class AbstractTest {
    @Autowired
    lateinit var objectManager : ObjectManager

    protected var personDescriptor : ObjectDescriptor? = null

    @BeforeEach
    fun setupSchema() {
        withTransaction {
            if ( objectManager.findDescriptor("person") == null) {
                val stringType = string().length(100)
                val intType = int()

                personDescriptor = objectManager.type("person")
                    .attribute("name", stringType)
                    .attribute("age", intType)

                    // relations

                    .relation("father", "person", Multiplicity.ONE)
                    //.relation("children", "person", Multiplicity.N)

                    .attribute("boolean", boolean())
                    .attribute("string", string())
                    .attribute("short", short())
                    .attribute("int", int())
                    .attribute("long", long())
                    .attribute("float", float())
                    .attribute("double", double())
                    .register()
            }
            else personDescriptor = objectManager.getDescriptor("person")
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