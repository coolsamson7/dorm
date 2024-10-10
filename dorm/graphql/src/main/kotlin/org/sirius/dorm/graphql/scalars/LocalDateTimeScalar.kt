package org.sirius.dorm.graphql.scalars
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import graphql.language.StringValue
import graphql.language.Value
import graphql.schema.*
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.function.Function

/**
 * Access this via [graphql.scalars.ExtendedScalars.DateTime]
 */
class LocalDateTimeScalar : Coercing<LocalDateTime, String> {
    // instance data

    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm") // TODO s?

    // implement

    @Throws(CoercingSerializeException::class)
    override fun serialize(input: Any): String {
        val offsetDateTime: LocalDateTime = if (input is LocalDateTime) {
            input
        }
        else if (input is String) {
            parse(input.toString()) { message: String? ->
                CoercingSerializeException(message)
            }
        }
        else {
            throw CoercingSerializeException("Expected something we can convert to 'java.time.LocalDateTime' but was '" + input.javaClass + "'.")
        }
        return try {
            offsetDateTime.format(formatter)
        }
        catch (e: DateTimeException) {
            throw CoercingSerializeException("Unable to turn TemporalAccessor into LocalDateTime because of : '" + e.message + "'.")
        }
    }

    @Throws(CoercingParseValueException::class)
    override fun parseValue(input: Any): LocalDateTime {
        val offsetDateTime: LocalDateTime = if (input is LocalDateTime) {
            input
        }
        else if (input is String) {
            parse(input.toString()) { message: String? ->
                CoercingParseValueException(message)
            }
        }
        else {
            throw CoercingParseValueException("Expected a 'String' but was '" + input.javaClass + "'.")
        }

        return offsetDateTime
    }

    @Throws(CoercingParseLiteralException::class)
    override fun parseLiteral(input: Any): LocalDateTime {
        if (input !is StringValue) {
            throw CoercingParseLiteralException("Expected AST type 'StringValue' but was '" + input.javaClass + "'.")
        }

        return parse(input.value) { message: String? ->
            CoercingParseLiteralException(message)
        }
    }

    override fun valueToLiteral(input: Any): Value<*> {
        val s = serialize(input)

        return StringValue.newStringValue(s).build()
    }

    // private

    private fun parse(s: String, exceptionMaker: Function<String, RuntimeException>): LocalDateTime {
        return try {
            LocalDateTime.parse(s, formatter)
        }
        catch (e: DateTimeParseException) {
            throw exceptionMaker.apply("Invalid RFC3339 value : '" + s + "'. because of : '" + e.message + "'")
        }
    }

    companion object {
        val INSTANCE = GraphQLScalarType.newScalar()
            .name("DateTime")
            .description("A slightly refined version of RFC-3339 compliant DateTime Scalar")
            .coercing(LocalDateTimeScalar())
            .build()
    }
}