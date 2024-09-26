package org.sirius.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.`object`.DataObject
import org.junit.jupiter.api.Test
import org.sirius.common.type.base.*
import org.sirius.dorm.model.Cascade
import org.sirius.dorm.model.Multiplicity
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.`object`.MultiValuedRelation
import org.sirius.dorm.`object`.Relation
import org.sirius.dorm.`object`.SingleValuedRelation
import org.sirius.dorm.query.eq
import kotlin.test.assertEquals


class RelationTests: AbstractTest() {
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
                .attribute("name", string())
                .relation("children", "pp", Multiplicity.ZERO_OR_MANY, "father")
                .relation("father", "pp", Multiplicity.ZERO_OR_ONE, "children")
                .register()
        }

        val descriptor =  objectManager.getDescriptor("pp")

        // create

        var id = 0L
        var childId = 0L

        withTransaction {
            val person = objectManager.create(descriptor)

            person["name"] = "Andi"

            id = person.id

            val child = objectManager.create(descriptor)

            childId = child.id

            person.relation<MultiValuedRelation>("children").add(child)

        }

        printTables()

        // reread

        withTransaction {
            val child = objectManager.findById(descriptor, childId)!!

            child["father"] = null

            // load father and check if the relation is empty

            val father = objectManager.findById(descriptor, id)!!

            assertEquals(0, father.relation<MultiValuedRelation>("children").size)
        }

        printTables()

        // other way round

        withTransaction {
            val father = objectManager.findById(descriptor, id)!!
            val child = objectManager.findById(descriptor, childId)!!

            father.relation<MultiValuedRelation>("children").add(child)

            // load father and check if the relation is empty

            val childFather = child["father"]

            assert(childFather == father)
        }
    }

    @Test
    fun testOneToMany() {
        // create schema

        withTransaction {
             objectManager.type("p")
                .attribute("name", string())
                .relation("children", "p", Multiplicity.ZERO_OR_MANY, "father")
                .relation("father", "p", Multiplicity.ZERO_OR_ONE, "children")
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

            val children = person.relation<MultiValuedRelation>("children")

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
                .attribute("name", string())
                .relation("parts", "part", Multiplicity.ZERO_OR_MANY, "product", Cascade.DELETE)
                .register()

            objectManager.type("part")
                .attribute("name", string())
                .relation("product", "product", Multiplicity.ONE)
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
            caughtError = true
        }

        assertEquals(true, caughtError)

        // now really

        var id = 0L
        withTransaction {
            val product = objectManager.create(productDescriptor)

            product["name"] = "Car"

            val part = objectManager.create(partDescriptor)

            part["name"] = "Motor"

            part["product"] = product

            id = product.id
        }

        printTables()

        // try delete

        withTransaction {
            val product = objectManager.findById(productDescriptor, id)!!

            objectManager.delete(product)
        }

        printTables()

        // check

        withTransaction {
            val queryManager = objectManager.queryManager()
            val part = queryManager.from(partDescriptor)

            // no where

            val query = queryManager
                .create()
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
                .attribute("name", string())
                .relation("parent", "p1", Multiplicity.ZERO_OR_ONE, "children")
                .relation("children", "p1", Multiplicity.ZERO_OR_MANY, "parent")
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

            person.value<MultiValuedRelation>("children").add(child)
            person.relation<MultiValuedRelation>("children").add(child)
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