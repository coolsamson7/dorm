package org.sirius.application
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import lombok.extern.slf4j.Slf4j
import org.sirius.dorm.DORMConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.*

@Configuration()
@Import(DORMConfiguration::class)
class ApplicationConfiguration {
}

@SpringBootApplication
@Slf4j
class DORMApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(DORMApplication::class.java, *args)
        }
    }
}
