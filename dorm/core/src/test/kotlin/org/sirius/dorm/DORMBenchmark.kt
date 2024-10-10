package org.sirius.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.query.eq
import jakarta.persistence.*
import org.junit.jupiter.api.Test
import org.sirius.common.type.base.*
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.attribute

internal class DORMBenchmark : AbstractTest() {
    // local

    protected fun measure(test: String, n: Int, doIt: () -> Unit) {
        println("### start " + test)
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

    @Test
    fun tes() {
        var personD : ObjectDescriptor?

        withTransaction {
            objectManager.type("small-person")
                .add(attribute("name").type(string()))
                .register()
        }

        val objects = 2000

        // create

        measure("create ${objects} objects ", objects) {
            personD = objectManager.findDescriptor("small-person")
            for (i in 1..objects) {
                val person1 = objectManager.create(personD!!)

                person1["name"] = "Andi"
            }
        }

        measure("read ${objects} objects ", objects) {
            personD = objectManager.findDescriptor("small-person")
            val queryManager = objectManager.queryManager()
            val person = queryManager.from(personD!!)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)

            query.execute().getResultList()
        }

        // filter

        measure("filter ${objects} objects ", objects) {
            personD = objectManager.findDescriptor("small-person")
            val queryManager = objectManager.queryManager()
            val person = queryManager.from(personD!!)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)
                .where(eq(person.get("name"), "Andi"))

            query.execute().getResultList()
        }

        // filter & projection

        measure("filter & project ${objects} objects ", objects) {
            personD = objectManager.findDescriptor("small-person")
            val queryManager = objectManager.queryManager()
            val person = queryManager.from(personD!!)

            // no where

            val query = queryManager
                .create()
                .select(person.get("name"))
                .from(person)
                .where(eq(person.get("name"), "Andi"))

            query.execute().getResultList()
        }

        measure("update ${objects} objects ", objects) {
            personD = objectManager.findDescriptor("small-person")
            val queryManager = objectManager.queryManager()
            val person = queryManager.from(personD!!)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)

            for (person in query.execute().getResultList())
                person["name"] = "Changed"
        }
    }

    /*@Test
    fun Foo() {
        // warm up

        withTransaction {
            val person = PersonEntity(0,"Andi", 58, false, "strung", 1, 1, 1, 1.0f, 1.0, mutableSetOf(), mutableSetOf(), mutableSetOf())
            val hobby = HobbyEntity(0, "angeln", mutableSetOf())

            entityManager.persist(hobby)

            //person.hobbies.add(hobby)

            entityManager.persist(person)
        }
    }

    @Test
    fun testJPA() {
        println("### JPA")

        // warm up

        withTransaction {
            entityManager.persist(PersonEntity(0,"Andi", 58, false, "strung", 1, 1, 1, 1.0f, 1.0, mutableSetOf(), mutableSetOf(), mutableSetOf()))

            val builder: CriteriaBuilder = entityManager.criteriaBuilder

            // update attributes

            val criteriaQuery = builder.createQuery(PersonEntity::class.java)
            val personEntity = criteriaQuery.from(PersonEntity::class.java)

            criteriaQuery
                .select(personEntity)
                //.where(builder.equal(attributeEntity.get<Int>("entity"), obj.id))

            val result = entityManager.createQuery(criteriaQuery).resultList
        }

        // let's go

        // create some objects

        val objects = 2000

        // create

        measure("create ${objects} objects ", objects) {
            for (i in 1..objects) {
                entityManager.persist(PersonEntity(0,"Andi", 58, false, "strung", 1, 1, 1, 1.0f, 1.0, mutableSetOf(), mutableSetOf(), mutableSetOf()))
            }
        }

        // read

        measure("read ${objects} objects ", objects) {
            val builder: CriteriaBuilder = entityManager.criteriaBuilder

            // update attributes

            val criteriaQuery = builder.createQuery(PersonEntity::class.java)
            val personEntity = criteriaQuery.from(PersonEntity::class.java)

            criteriaQuery
                .select(personEntity)
                //.where(eq(attributeEntity.get<Int>("entity"), obj.id))

            val result = entityManager.createQuery(criteriaQuery).resultList
        }

        measure("filter ${objects} objects ", objects) {
            val builder: CriteriaBuilder = entityManager.criteriaBuilder

            // update attributes

            val criteriaQuery = builder.createQuery(PersonEntity::class.java)
            val personEntity = criteriaQuery.from(PersonEntity::class.java)

            criteriaQuery
                .select(personEntity)
                .where(builder.equal(personEntity.get<Int>("name"), "Andi"))

            val result = entityManager.createQuery(criteriaQuery).resultList
        }

        measure("filter & project ${objects} objects ", objects) {
            val builder = entityManager.criteriaBuilder

            // update attributes

            val criteriaQuery = builder.createQuery(Array<Any>::class.java)
            val personEntity = criteriaQuery.from(PersonEntity::class.java)

            criteriaQuery
                .multiselect(personEntity.get<String>("name"), personEntity.get<Int>("age"))
                .where(builder.equal(personEntity.get<Int>("name"), "Andi"))

            val result = entityManager.createQuery(criteriaQuery).resultList

            System.err.println()
        }

        // update

        measure("update ${objects} objects ", objects) {
            val builder: CriteriaBuilder = entityManager.criteriaBuilder

            // update attributes

            val criteriaQuery = builder.createQuery(PersonEntity::class.java)
            val personEntity = criteriaQuery.from(PersonEntity::class.java)

            criteriaQuery
                .select(personEntity)
            //.where(builder.equal(attributeEntity.get<Int>("entity"), obj.id))

            for (person in entityManager.createQuery(criteriaQuery).resultList)
                person.name = "Changed"
        }
    }
*/
    @Test
    fun test() {
        println("### DORM")

        // warm up

        createPerson("Andi", 58)
        withTransaction { -> readPersons() }

        // create some objects

        val objects = 2000

        // create

        measure("create ${objects} objects ", objects) {
            for (i in 1..objects) {
                val person1 = objectManager.create(personDescriptor!!)

                person1["name"] = "Andi"
                person1["age"] = 58
            }
        }

        // read

        measure("read ${objects} objects ", objects) {
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

        measure("filter ${objects} objects ", objects) {
            val queryManager = objectManager.queryManager()
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)
                .where(eq(person.get("name"), "Andi"))

            query.execute().getResultList()
        }

        // filter & projection

        measure("filter & project ${objects} objects ", objects) {
            val queryManager = objectManager.queryManager()
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager
                .create()
                .select(person.get("name"), person.get("age"))
                .from(person)
                .where(eq(person.get("name"), "Andi"))

            query.execute().getResultList()
        }

        measure("update ${objects} objects ", objects) {
            val queryManager = objectManager.queryManager()
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager
                .create()
                .select(person)
                .from(person)

            for (person in query.execute().getResultList())
                person["name"] = "Changed"
        }
    }

}