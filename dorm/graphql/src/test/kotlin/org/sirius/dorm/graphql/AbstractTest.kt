package org.sirius.dorm.graphql
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.common.tracer.TraceLevel
import org.sirius.common.tracer.Tracer
import org.sirius.common.tracer.trace.ConsoleTrace

import org.sirius.dorm.DORMConfiguration
import org.sirius.dorm.ObjectManager

import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.session.DummySessionContextProvider
import org.sirius.dorm.session.SessionContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Component

@Component
class DummySessionContext : SessionContext(DummySessionContextProvider("me")) {}

@Configuration()
@Import(DORMConfiguration::class)
class GraphQLTestConfiguration {
}

@SpringBootTest(classes =[GraphQLTestConfiguration::class])
abstract class AbstractTest {
    @Autowired
    lateinit var objectManager : ObjectManager

    protected var personDescriptor : ObjectDescriptor? = null

    init {
        Tracer(ConsoleTrace(), "%t{yyyy-MM-dd HH:mm:ss,SSS} %l{-10s} [%p] %m")
            .setTraceLevel("", TraceLevel.OFF)
            .setTraceLevel("com", TraceLevel.LOW)
            .setTraceLevel("com.sirius.dorm", TraceLevel.HIGH)
    }

    protected fun createPerson(name: String, age: Int) : Long {
        objectManager.begin()
        try {
            val person = objectManager.create(personDescriptor!!)

            person["name"] = name
            person["age"] = age

            return person.id
        }
        finally {
            objectManager.commit()
        }
    }

    protected fun withTransaction(doIt: () -> Unit) {
        objectManager.begin()
        var committed = false
        try {
            doIt()

            committed = true
            objectManager.commit()
        }
        catch (throwable: Throwable) {
            if ( !committed )
                objectManager.rollback()

            throw throwable
        }
    }

    protected fun measure(test: String, n: Int, doIt: () -> Unit) {
        println("> $test")
        val start = System.currentTimeMillis()

        var committed = false
        //objectManager.begin()
        try {
            doIt()

            committed = true
            //objectManager.commit()
        }
        catch (throwable: Throwable) {
            //if ( !committed )
            //    objectManager.rollback()

            throw throwable
        }
        finally {
            val ms = System.currentTimeMillis() - start
            val avg = ms.toFloat() / n

            println("< ${ms}ms, avg: ${avg}")
        }
    }
}