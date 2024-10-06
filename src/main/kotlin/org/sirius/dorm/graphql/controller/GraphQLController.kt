package org.sirius.dorm.graphql.controller
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.ExecutionInput
import graphql.GraphQL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.io.IOException


class TRe : TypeReference<Map<String?, Any?>>(){

}

@RestController
class GraphQLController @Autowired constructor(private val graphql: GraphQL, private val objectMapper: ObjectMapper) {
    @RequestMapping(value = ["/graphql"], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    @CrossOrigin
    @Throws(
        IOException::class
    )
    fun query(
        @RequestParam("query") query: String,
        @RequestParam(value = "operationName", required = false) operationName: String?,
        @RequestParam("variables") variablesJson: String?): Map<String, Any> {
        var variables: Map<String?, Any?> = LinkedHashMap()
        if (variablesJson != null)
            variables = objectMapper.readValue(variablesJson, TRe())

        return executeGraphqlQuery(query, operationName, variables)
    }

    @RequestMapping(value = ["/graphql"], method = [RequestMethod.POST], produces = [MediaType.APPLICATION_JSON_VALUE])
    @CrossOrigin
    fun graphql(@RequestBody body: Map<String?, Any?>): Map<String, Any> {
        var query = body["query"] as String?
        if (query == null)
            query = ""

        val operationName = body["operationName"] as String?
        var variables = body["variables"] as Map<String?, Any?>?
        if (variables == null)
            variables = LinkedHashMap()

        return executeGraphqlQuery(query, operationName, variables)
    }

    // private

    private fun executeGraphqlQuery(query: String, operationName: String?, variables: Map<String?, Any?>): Map<String, Any> {
        val executionInput = ExecutionInput.newExecutionInput()
            .query(query)
            .operationName(operationName)
            .variables(variables)
            .build()

        return graphql.execute(executionInput).toSpecification()
    }
}