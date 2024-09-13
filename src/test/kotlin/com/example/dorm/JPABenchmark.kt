package com.example.dorm

import com.example.dorm.type.base.int
import com.example.dorm.type.base.string
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [JPAConfiguration::class])
internal class JPABenchmark {
    @Autowired
    lateinit var manager : ObjectManager

    @Test
    fun test() {
        // setup schema

        val stringType = string().length(100)
        val intType = int()

        val personDescriptor = manager.type("person")
            .attribute("name", stringType)
            .attribute("age", intType)
            .attribute("v1", stringType)
            .attribute("v2", stringType)
            .attribute("v3", stringType)
            .register()

        // warm up

        manager.begin()
        try {
            val person1 = manager.create(personDescriptor)

            person1["name"] = "Andi"
            person1["age"] = 58
            person1["v1"] = "v1"
            person1["v2"] = "v2"
            person1["v3"] = "v3"
        }
        finally {
            manager.commit()
        }

        val queryManager = manager.queryManager()

        manager.begin()
        try {
            val person = queryManager.from(personDescriptor)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)

            val queryResult = query.execute().getResultList()
        }
        finally {
            manager.commit()
        }

        // create some objects

        val objects = 1000

        // write

        var start = System.currentTimeMillis()

        manager.begin()
        try {
            for ( i in 1..objects) {
                val person1 = manager.create(personDescriptor)

                person1["name"] = "Andi"
                person1["age"] = 58
                person1["v1"] = "v1"
                person1["v2"] = "v2"
                person1["v3"] = "v3"
            }
        }
        finally {
            manager.commit()
        }

        var ms = System.currentTimeMillis() - start
        var avg = ms.toFloat() / objects

        println("create ${objects} took ${ms}ms, avg: ${avg}")

        // read

        start = System.currentTimeMillis()

        manager.begin()
        try {
            val person = queryManager.from(personDescriptor)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)

            val queryResult = query.execute().getResultList()
        }
        finally {
            manager.commit()
        }

        ms = System.currentTimeMillis() - start
        avg = ms.toFloat() / objects

        println("read ${objects} took ${ms}ms, avg: ${avg}")

        // read with filter

        start = System.currentTimeMillis()

        manager.begin()
        try {
            val person = queryManager.from(personDescriptor)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)

            query.where(query.gt(person.get("age"), 0))

            val queryResult = query.execute().getResultList()
        }
        finally {
            manager.commit()
        }

        ms = System.currentTimeMillis() - start
        avg = ms.toFloat() / objects

        println("read with filter ${objects} took ${ms}ms, avg: ${avg}")

        // update

        start = System.currentTimeMillis()

        manager.begin()
        try {
            val person = queryManager.from(personDescriptor)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)

            val queryResult = query.execute().getResultList()

            for ( person in queryResult)
                person["name"] = (person["name"] as String) + "1"
        }
        finally {
            manager.commit()
        }

        ms = System.currentTimeMillis() - start
        avg = ms.toFloat() / objects

        println("update ${objects} took ${ms}ms, avg: ${avg}")
    }
}