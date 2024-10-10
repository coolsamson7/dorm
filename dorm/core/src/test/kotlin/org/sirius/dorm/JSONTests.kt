package org.sirius.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


class JSONTests: AbstractTest() {
    // instance data

    @Autowired
    lateinit var mapper : ObjectMapper

    @Test
    fun test() {
        // create and write

        var json = ""
        withTransaction {
            val p1 = objectManager.create(personDescriptor!!)

            p1["name"] = "Andi"

            val p2 = objectManager.create(personDescriptor!!)

            p2["name"] = "Nika"

            p2["father"] = p1

            json = mapper.writeValueAsString(p2)
        }

        // delete

        deletePerson()

       // withTransaction {
       //     mapper.readValue(json, DataObject::class.java)
       // }

        // read
    }
}