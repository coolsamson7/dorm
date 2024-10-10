package org.sirius.application
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

//import lombok.extern.slf4j.Slf4j
import org.sirius.dorm.DORMConfiguration
import org.sirius.dorm.session.SessionContext
import org.sirius.dorm.session.SessionContextProvider
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.*


class DefaultSessionContextProvider : SessionContextProvider {
    override fun getUser(): String {
        return "me"
    }
}
@Configuration()
@Import(DORMConfiguration::class)
class ApplicationConfiguration {
    @Bean
    @Primary
    fun session() : SessionContext {
        return SessionContext(DefaultSessionContextProvider())
    }
}

@SpringBootApplication
//@Slf4j
class DORMApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(DORMApplication::class.java, *args)
        }
    }
}
