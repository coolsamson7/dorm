package com.quasar.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.json.ObjectModule
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
@ComponentScan
class DORMConfiguration {
    //@Bean
    fun objectModule(): ObjectModule {
        return ObjectModule()
    }
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {

        return ObjectMapper()
            //setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .registerModule( ObjectModule())
    }

}

// TODO....if we remove it, spring will not boot....

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
