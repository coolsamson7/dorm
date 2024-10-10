package org.sirius.dorm.graphql
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import graphql.GraphQL
import jakarta.annotation.PostConstruct
import org.junit.jupiter.api.RepeatedTest
import org.sirius.dorm.DORMConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.junit.jupiter.api.Test
import org.sirius.dorm.ObjectManager
import org.sirius.dorm.graphql.test.TestData

@Configuration()
@Import(DORMConfiguration::class)
class TestConfiguration {
    @Autowired
    lateinit var objectManager : ObjectManager
    @PostConstruct
    fun createData() {
        TestData(objectManager)
    }
}

@SpringBootTest(classes =[TestConfiguration::class])
class GraphQLTest : AbstractTest() {
    @Autowired
    lateinit var graphQL : GraphQL

    @RepeatedTest(3)
    fun test() {
        measure("simple read", 2000) {
            for ( i in 0..2000) {
                val result = graphQL.execute(
                    "query sampleQuery {\n" +
                            "    Human(filter: { id: { eq: 1 } }) {\n" +
                            "        versionCounter\n" +
                            "        name\n" +
                            "        father {\n" +
                            "            name\n" +
                            "        }\n" +
                            "        id\n" +
                            "        name\n" +
                            "    }\n" +
                            "}"
                )
            }
        }
    }
}