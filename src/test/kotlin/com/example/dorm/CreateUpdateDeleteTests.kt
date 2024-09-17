package com.example.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class CreateUpdateDeleteTests: AbstractTest() {
    @Test
    fun testCreate() {
        createPerson("Andi", 58)
        createPerson("Sandra", 52)

        // update

        withTransaction {
            val persons = readPersons()

            assertEquals(2, persons.size)
        }
    }

    @Test
    fun testFind() {
        createPerson("Andi", 58)

        // update

        withTransaction {
            val persons = readPersons()

            assertEquals(1, persons.size)

            val person = persons[0]
            val queryPerson = objectManager.findById(personDescriptor!!, person.getId())!!

            assertEquals(person.getId(), queryPerson.getId())

            // TODO: it should be the same persons!!!!!!
        }
    }

    @Test
    fun testUpdate() {
        createPerson("Andi", 58)

        // update

        withTransaction {
            val persons = readPersons()

            assertEquals(1, persons.size)

            persons[0]["name"] = "Changed"
        }

        // reread

        withTransaction {
            val persons = readPersons()

            assertEquals(1, persons.size)

            assertEquals("Changed", persons[0]["name"])
        }
    }

    @Test
    fun testDelete() {
        // TODO
    }
}