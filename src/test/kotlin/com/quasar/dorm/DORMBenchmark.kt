package com.quasar.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.query.eq
import jakarta.persistence.*
import jakarta.persistence.criteria.CriteriaBuilder
import org.junit.jupiter.api.Test

@Entity
@Table(name="HOBBY")
data class HobbyEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id : Int,

    @Column(name = "NAME")
    var name : String,

    //@ManyToMany(mappedBy = "hobbies")
    //val persons : Set<PersonEntity>
)

@Entity
@Table(name="PERSON")
data class PersonEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id : Int,

    @Column(name = "NAME")
    var name : String,

    @Column(name = "AGE")
    var age : Int,

    @Column(name = "V1")
    var v1 : String,

    @Column(name = "V2")
    var v2 : String,

    @Column(name = "V3")
    var v3 : String,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "person_hobby",
        joinColumns = arrayOf(JoinColumn(name = "person_id")),
        inverseJoinColumns = arrayOf(JoinColumn(name = "hobby_id")))
    val hobbies : Set<HobbyEntity>
)

internal class DORMBenchmark : AbstractTest() {
    // instance data

    @PersistenceContext
    private lateinit var entityManager: EntityManager

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
    fun testRelation() {
        var id = 0
        withTransaction {
            val hobby = HobbyEntity(0, "angeln")

            entityManager.persist(hobby)

            val person = PersonEntity(0, "Andi", 58, "v1", "v2", "v3", setOf(hobby))

            entityManager.persist(person)

            id = person.id

        }

        withTransaction {
            val person = entityManager.find(PersonEntity::class.java, id)

            val hobbies = person.hobbies

            println()
        }
    }

    @Test
    fun testJPA() {
        // warm up

        withTransaction {
            entityManager.persist(PersonEntity(0,"Andi", 58, "v1", "v2", "v3", setOf()))

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

        val objects = 1000

        // create

        measure("create ${objects} objects ", objects) {
            for (i in 1..objects) {
                entityManager.persist(PersonEntity(0,"Andi", 58, "v1", "v2", "v3", setOf()))
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
            val builder: CriteriaBuilder = entityManager.criteriaBuilder

            // update attributes

            val criteriaQuery = builder.createQuery(PersonEntity::class.java)
            val personEntity = criteriaQuery.from(PersonEntity::class.java)

            criteriaQuery
                .select(personEntity.get("name"))//, personEntity.get("age"))
                .where(builder.equal(personEntity.get<Int>("name"), "Andi"))

            val result = entityManager.createQuery(criteriaQuery).resultList
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

    @Test
    fun test() {
        // warm up

        createPerson("Andi", 58)
        withTransaction { -> readPersons() }

        // create some objects

        val objects = 1000

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