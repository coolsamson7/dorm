package org.sirius.application
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import jakarta.annotation.PostConstruct
import org.sirius.dorm.DORMConfiguration
import org.sirius.dorm.ObjectManager
import org.sirius.dorm.graphql.test.TestData
import org.sirius.dorm.session.DummySessionContextProvider
import org.sirius.dorm.session.SessionContext
import org.sirius.dorm.session.SessionContextProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.*
import org.springframework.stereotype.Component

@Component
class DummySessionContext : SessionContext(DummySessionContextProvider("me")) {}

@Configuration()
@ComponentScan
@Import(DORMConfiguration::class)
class ApplicationConfiguration {
    @Autowired
    lateinit var objectManager: ObjectManager

    @PostConstruct
    fun createData() {
        TestData(objectManager)
    }
}

@SpringBootApplication
class DORMApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(DORMApplication::class.java, *args)
        }
    }
}
