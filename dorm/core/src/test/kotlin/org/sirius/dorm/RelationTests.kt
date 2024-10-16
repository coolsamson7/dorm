package org.sirius.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.h2.tools.Server
import org.sirius.dorm.`object`.DataObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.sirius.common.type.base.*
import org.sirius.dorm.model.*
import org.sirius.dorm.`object`.MultiValuedRelation
import kotlin.test.assertEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RelationTests: AbstractTest() {
    //@BeforeAll
    fun setup() {
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082")
            .start();
    }


    @Test
    fun testOneToOne() {
        var id = 0L

        withTransaction {
            val andi = objectManager.create(personDescriptor!!)

            andi["name"] = "Andi"
            andi["age"] = 58

            val helmut = objectManager.create(personDescriptor!!)

            helmut["name"] = "Helmut"
            helmut["age"] = 89

            andi["father"] = helmut

            id = andi.id
        }

        // update

        withTransaction {
            val andi = objectManager.findById(personDescriptor!!, id)

            assert(andi !== null)

            val father = andi!!["father"] as DataObject?

            assert(father !== null)

            assertEquals("Helmut", father!!["name"])
        }
    }

    @Test
    fun testSynchronization() {
        withTransaction {
            objectManager.type("pp")
                .add(attribute("name").type(string()))
                .add(relation("children").target("pp").multiplicity(Multiplicity.ZERO_OR_MANY).inverse("father"))
                .add(relation("father").target("pp").multiplicity(Multiplicity.ZERO_OR_ONE).inverse("children"))
                .register()
        }

        val descriptor =  objectManager.getDescriptor("pp")

        // create

        var id = 0L
        var childId = 0L

        println("### add child")

        withTransaction {
            val person = objectManager.create(descriptor)

            person["name"] = "Andi"

            id = person.id

            val child = objectManager.create(descriptor)

            child["name"] = "Nika"

            childId = child.id

            person.relation("children").add(child)
        }

        printTables() // expect 1 relation

        // reread

        println("### remove child")

        withTransaction {
            val child = objectManager.findById(descriptor, childId)!!

            assert(child["father"] !== null)

            child["father"] = null

            // load father and check if the relation is empty

            val father = objectManager.findById(descriptor, id)!!

            assertEquals(0, father.relation("children").size)
        }

        printTables() // expect empty relation

        // other way round

        println("### add child")

        withTransaction {

            val father = objectManager.findById(descriptor, id)!!
            val child = objectManager.findById(descriptor, childId)!!

            val children = father.relation("children")
            val f = child["father"]

            assert( father.relation("children").size == 0)

            father.relation("children").add(child)

            // load father and check if the relation is empty

            val childFather = child["father"]

            assert(childFather == father)
        }

        printTables() // expect 1 relation

        withTransaction {

            val father = objectManager.findById(descriptor, id)!!

            assert(father.relation("children").size == 1)
        }
    }

    @Test
    fun testOneToMany() {
        // create schema

        withTransaction {
             objectManager.type("p")
                .add(attribute("name").type(string()))
                .add(relation("children").target("p").multiplicity(Multiplicity.ZERO_OR_MANY).inverse("father"))
                .add(relation("father").target("p").multiplicity(Multiplicity.ZERO_OR_ONE).inverse("children"))
                .register()
        }

        val descriptor =  objectManager.getDescriptor("p")

        // test

        var id = 0L

        withTransaction {
            val person = objectManager.create(descriptor)

            person["name"] = "Andi"

            // force load

            //person["children"]

            id = person.id

            // child 1

            val child1 = objectManager.create(descriptor)

            child1["name"] = "Nika"
            child1["father"] = person

            val father1 = child1["father"]

            // child 2

            val child2 = objectManager.create(descriptor)

            child2["name"] = "Pupsi"
            child2["father"] = person

            val father2 = child2["father"]

            // person children

            val children = person.relation("children")

            // should have synchronized in memory already

            assertEquals(2, children.size)

            // add as child

            //person.value<MultiValuedRelation>("children").add(child)
            //person.relation("children").add(child)
        }

        printTables()

        // reread

        withTransaction {
            val person = objectManager.findById(descriptor, id)

            assert(person !== null)

            assert(person!!["children"] !== null)

            val children = person!!["children"] as MultiValuedRelation

            assert(children.size == 2)

            val iter = children.iterator()
            val x = iter.next()
            val y = iter.next()

            //assertEquals("Nika", children[0]["name"])
        }
    }

    @Test
    fun testValidateRelation() {
        // create schema

        withTransaction {
            objectManager.type("product")
                .add(attribute("name").type(string()))
                .add(relation("parts").target("part").multiplicity(Multiplicity.ZERO_OR_MANY).inverse("product").cascadeDelete().removeOrphans())
                .register()

            objectManager.type("part")
                .add(attribute("name").type(string()))
                .add(relation("product").target("product").inverse("parts").multiplicity(Multiplicity.ONE))
                .register()
        }

        val productDescriptor = objectManager.getDescriptor("product")
        val partDescriptor = objectManager.getDescriptor("part")

        // test

        var caughtError = false

        try {
            withTransaction {
                val product = objectManager.create(productDescriptor)

                product["name"] = "Car"

                val part = objectManager.create(partDescriptor)

                part["name"] = "Motor"

                //part["product"] = product
            }
        }
        catch(exception: ObjectManagerError) {
            // it should throw since the relation is mandatory!!!
            caughtError = true
        }

        printTables()

        assertEquals(true, caughtError)

        // now really

        var id = 0L
        withTransaction {
            val product = objectManager.create(productDescriptor)

            product["name"] = "Car"

            val part = objectManager.create(partDescriptor)

            part["name"] = "Motor"

            part["product"] = product

            val part2 = objectManager.create(partDescriptor)

            part2["name"] = "Something"

            part2["product"] = product

            id = product.id
        }

        printTables()

        // check orphan removal

        withTransaction {
            val product = objectManager.findById(productDescriptor, id)!!

            for ( part in product.relation("parts"))
                if ( part["name"] == "Something") {
                    part["product"] = null
                    break
                }
        }

        printTables()

        // try delete

        withTransaction {
            val queryManager = objectManager.queryManager()
            val part = queryManager.from(partDescriptor)

            // no where

            val query = queryManager
                .query()
                .select(part)
                .from(part)

            val result = query.executor()
                .execute()
                .getResultList()

            assertEquals(1, result.size)

            val product = objectManager.findById(productDescriptor, id)!!

            objectManager.delete(product)
        }

        printTables()

        // check cascading delete

        withTransaction {
            val queryManager = objectManager.queryManager()
            val part = queryManager.from(partDescriptor)

            // no where

            val query = queryManager
                .query()
                .select(part)
                .from(part)

            val result = query.executor()
                .execute()
                .getResultList()

            assertEquals(0, result.size)
        }
    }

    @Test
    fun testOneToManyIncludingInverse() {
        // create schema

        withTransaction {
             objectManager.type("p1")
                .add(attribute("name").type(string()))
                .add(relation("parent").target("p1").multiplicity(Multiplicity.ZERO_OR_ONE).inverse("children"))
                .add(relation("children").target("p1").multiplicity(Multiplicity.ZERO_OR_MANY).inverse("parent"))
                .register()
        }

        val descriptor =  objectManager.findDescriptor("p1")

        // test

        var id = 0L

        withTransaction {
            val person = objectManager.create(descriptor!!)

            person["name"] = "Andi"

            id = person.id

            val child = objectManager.create(descriptor)

            child["name"] = "Nika"

            // add as child

            person.relation("children").add(child)
            person.relation("children").add(child)
        }

        printTables()

        // reread

        withTransaction {
            val person = objectManager.findById(descriptor!!, id)

            assert(person !== null)

            assert(person!!["children"] !== null)

            val children : MultiValuedRelation = person!!["children"] as MultiValuedRelation

            assert(children.size == 1)
            //assertEquals("Nika", children[0]["name"])
        }
    }
}