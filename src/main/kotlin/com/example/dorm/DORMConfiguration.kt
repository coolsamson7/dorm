package com.example.dorm

import com.example.dorm.json.ObjectModule
import com.fasterxml.jackson.annotation.JsonInclude
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

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
