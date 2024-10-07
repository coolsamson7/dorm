package org.sirius.dorm.graphql.test
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import jakarta.annotation.PostConstruct
import org.sirius.common.type.base.*
import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.Multiplicity
import org.sirius.dorm.model.attribute
import org.sirius.dorm.model.relation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class TestData {
    @Autowired
    lateinit var objectManager: ObjectManager

    // protected

    protected fun <T> withTransaction(doIt: () -> T) : T {
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

    @PostConstruct
    fun setupData() {
        withTransaction {
            // create type

            objectManager.type("Person")
                .add(attribute("firstName").type(string()))
                .add(attribute("name").type(string()))
                .add(attribute("age").type(int()))

                .add(attribute("short").type(short()))
                .add(attribute("long").type(long()))
                .add(attribute("float").type(float()))
                .add(attribute("double").type(double()))

                // relations

                .add(relation("father").target("Person").multiplicity(Multiplicity.ZERO_OR_ONE).inverse("children"))
                .add(relation("children").target("Person").multiplicity(Multiplicity.ZERO_OR_MANY).inverse("father").owner())

                // done

                .register()

            // create data

            val personDescriptor = objectManager.getDescriptor("Person")

            val andi = objectManager.create(personDescriptor)

            andi["firstName"] = "Andi"
            andi["name"] = "Ernst"
            andi["age"] = 58

            // child

            val child = objectManager.create(personDescriptor)

            child["firstName"] = "Nika"
            child["name"] = "Martinez"
            child["age"] = 14

            // link

            child["father"] = andi
        }
    }
}