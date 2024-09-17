package com.example.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.example.dorm.model.ObjectDescriptor
import com.example.dorm.type.base.int
import com.example.dorm.type.base.string
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


//@ActiveProfiles("local")
//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes =[TestConfiguration::class])
//@TestPropertySource(properties = {"spring.config.location=classpath:application"})
//@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
class AbstractTest {
    @Autowired
    lateinit var objectManager : ObjectManager

    protected var personDescriptor : ObjectDescriptor? = null

    @BeforeEach
    fun setupSchema() {
        if ( objectManager.find("person") == null) {
            val stringType = string().length(100)
            val intType = int()

            personDescriptor = objectManager.type("person")
                .attribute("name", stringType)
                .attribute("age", intType)
                .attribute("v1", stringType)
                .attribute("v2", stringType)
                .attribute("v3", stringType)
                .register()
        }
        else personDescriptor = objectManager.get("person")
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
            person1["v1"] = "v1"
            person1["v2"] = "v2"
            person1["v3"] = "v3"
        }
        finally {
            objectManager.commit()
        }
    }
}