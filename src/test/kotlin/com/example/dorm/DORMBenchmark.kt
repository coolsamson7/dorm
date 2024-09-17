package com.example.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.junit.jupiter.api.Test

internal class DORMBenchmark : AbstractTest() {
    // local

    protected fun measure(test: String, n: Int, doIt: () -> Unit) {
        val start = System.currentTimeMillis()

        objectManager.begin()
        try {
            doIt()
        }
        finally {
            objectManager.commit()

            val ms = System.currentTimeMillis() - start
            val avg = ms.toFloat() / n

            println("executed ${test} in ${ms}ms, avg: ${avg}")
        }
    }

    // test

    @Test
    fun test() {
        // warm up

        createPerson("Andi", 58)
        withTransaction { -> readPersons() }

        // create some objects

        val objects = 1000

        // create

        measure("create objects ", objects) {
            for (i in 1..objects) {
                val person1 = objectManager.create(personDescriptor!!)

                person1["name"] = "Andi"
                person1["age"] = 58
                person1["v1"] = "v1"
                person1["v2"] = "v2"
                person1["v3"] = "v3"
            }
        }

        // read

        measure("read objects ", objects) {
            val queryManager = objectManager.queryManager()
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)

            query.execute().getResultList()
        }

        // filter

        measure("read objects ", objects) {
            val queryManager = objectManager.queryManager()
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)

            query.where(query.eq(person.get("name"), "Andi"))

            query.execute().getResultList()
        }

        measure("update objects ", objects) {
            val queryManager = objectManager.queryManager()
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)

            query.where(query.eq(person.get("name"), "Andi"))

            for (person in query.execute().getResultList())
                person["name"] = "Changed"
        }
    }
}