package org.sirius.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.`object`.DataObject
import org.junit.jupiter.api.Test
import org.sirius.common.type.base.*
import org.sirius.dorm.model.Multiplicity
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.`object`.MultiValuedRelation
import kotlin.test.assertEquals


class RelationTests: AbstractTest() {
    @Test
    fun testOneToOne() {
        var id = 0

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
    fun testOneToMany() {
        // create schema

        var descriptor : ObjectDescriptor?= null
        withTransaction {
            descriptor = objectManager.type("p")
                .attribute("name", string())
                .relation("children", "p", Multiplicity.ZERO_OR_MANY)
                .register()
        }

        // test

        var id = 0

        withTransaction {
            val person = objectManager.create(descriptor!!)

            person["name"] = "Andi"

            id = person.id

            val child = objectManager.create(descriptor!!)

            child["name"] = "Nika"

            // add as child

            person.value<MultiValuedRelation>("children").add(child)
            person.relation("children").add(child)
        }

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