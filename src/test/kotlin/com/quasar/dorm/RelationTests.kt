package com.quasar.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.query.and
import org.junit.jupiter.api.Test
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

            assertEquals("helmut", father!!["name"])
        }
    }
}