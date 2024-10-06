package org.sirius.dorm.graphql
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import lombok.extern.slf4j.Slf4j
import org.sirius.dorm.DORMConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import


@Configuration()
@Import(DORMConfiguration::class)
class GraphQLApplicationConfiguration {
}


/*@Configuration
class GraphiQlConfiguration {
    @Bean
    @Order(0)
    fun graphiQlRouterFunction(): RouterFunction<ServerResponse> {
        var builder = RouterFunctions.route()
        val graphiQlPage = ClassPathResource("graphiql/index.html")
        val graphiQLHandler = GraphiQlHandler("/graphql", "", graphiQlPage)
        builder = builder.GET("/graphiql", graphiQLHandler::handleRequest)
        return builder.build()
    }
}*/
@SpringBootApplication
@Slf4j
class GraphQLApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(GraphQLApplication::class.java, *args)
        }
    }
}
