package org.sirius.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.junit.jupiter.api.Test
import org.sirius.common.type.ValidationError
import kotlin.test.assertEquals


class CreateUpdateDeleteTests: AbstractTest() {
    @Test
    fun testOne() {
        val id = createPerson("Andi", 58)

        printTables()

        withTransaction {
            val person = objectManager.findById(personDescriptor!!, id)!!

            assertEquals("Andi", person["name"])
            assertEquals(58, person["age"])
        }
    }

    @Test
    fun testValidation() {
        var caughtError = false
        try {
            withTransaction {
                val person = objectManager.create(personDescriptor!!)

                person["name"] = "Andi"
                person["age"] = -1
            }
        }
        catch(e: ValidationError) {
            caughtError = true
        }

        assertEquals(true, caughtError)
    }

    @Test
    fun testCreate() {
        val andiId = createPerson("Andi", 58)
        val sandraId = createPerson("Sandra", 52)

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
            val queryPerson = objectManager.findById(personDescriptor!!, person.id)

            assert(queryPerson !== null)

            assertEquals(person.id, queryPerson!!.id)

            assert(person === queryPerson)
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