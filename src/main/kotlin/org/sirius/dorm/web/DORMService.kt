package org.sirius.dorm.web
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.sirius.common.type.base.*
import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.Multiplicity
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.attribute
import org.sirius.dorm.model.relation
import org.sirius.dorm.`object`.DataObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("dorm/")
@Component
class DORMService {
    // instance data

    @Autowired
    lateinit var objectManager: ObjectManager
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @PostConstruct
    fun setup() {
        withTransaction {
            objectManager.type("person")
                .add(attribute("name").type( string().length(100)))
                .add(attribute("age").type( int().greaterEqual(0)))

                // relations

                .add(relation("father").target("person").multiplicity(Multiplicity.ZERO_OR_ONE).inverse("children"))
                .add(relation("children").target("person").multiplicity(Multiplicity.ZERO_OR_MANY).inverse("father").owner())

                .add(attribute("boolean").type(boolean()))
                .add(attribute("string").type(string()))
                .add(attribute("short").type(short()))
                .add(attribute("int").type(int()))
                .add(attribute("long").type(long()))
                .add(attribute("float").type(float()))
                .add(attribute("double").type(double()))
                .register()

            val obj = objectManager.create(objectManager.getDescriptor("person"))

            obj["name"] = "Andi"
            obj["age"] = 58

            val child = objectManager.create(objectManager.getDescriptor("person"))

            child["name"] = "Nika"
            child["age"] = 14

            obj.relation("children").add(child)
        }
    }

    // private

    private fun <T> withTransaction(doIt: () -> T) : T {
        objectManager.begin()
        var committed = false
        try {
            val result = doIt()

            committed = true
            objectManager.commit()

            return result
        }
        catch (throwable: Throwable) {
            if ( !committed )
                objectManager.rollback()

            throw throwable
        }
    }

    // meta data methods

    @GetMapping("/spec/{type}")
    @ResponseBody
    fun spec(@PathVariable type: String): ObjectDescriptor {
        return withTransaction {
            return@withTransaction objectManager.getDescriptor(type)
        }
    }

    // object methods

    @GetMapping("/query/{query}")
    @ResponseBody
    fun query(@PathVariable query: String): String {
        return withTransaction {
            val resultList = objectManager
                .query(query, DataObject::class.java)
                .execute()
                .getResultList()

            return@withTransaction objectMapper.writeValueAsString(resultList)
        }
    }

    @PostMapping("/update")
    @ResponseBody
    fun update(@RequestBody request: String) {
        withTransaction {
            objectMapper.readValue(request, Array<DataObject>::class.java)
        }
    }
}