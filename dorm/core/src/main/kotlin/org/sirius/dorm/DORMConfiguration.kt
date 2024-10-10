package org.sirius.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.json.ObjectModule
import com.fasterxml.jackson.databind.ObjectMapper
import org.sirius.dorm.model.json.ObjectDescriptorModule
import org.sirius.common.type.json.TypeModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.*


@Configuration
@ComponentScan
@EntityScan
class DORMConfiguration {
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
            //setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .registerModule(ObjectDescriptorModule())
            .registerModule(ObjectModule())
            .registerModule(TypeModule())
    }

}

// TODO....if we remove it, spring will not boot....

@SpringBootApplication
class DemoApplication {
    fun main(args: Array<String>) {
        //runApplication<DemoApplication>(*args)
    }
}