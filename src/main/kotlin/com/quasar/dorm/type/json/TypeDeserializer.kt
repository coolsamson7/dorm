package com.quasar.dorm.type.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.quasar.dorm.type.Type
import com.quasar.dorm.type.base.*

open class TypeDeserializer() : StdDeserializer<Type<*>>(Type::class.java) {
    // override

    override fun deserialize(jsonParser: JsonParser, context: DeserializationContext): Type<*> {
        val node: JsonNode = jsonParser.codec.readTree(jsonParser)

        val type = node.get("type").asText()

        // create base instance

        val result = when (type.toLowerCase()) {
            "short" -> ShortType()
            "integer" -> IntType()
            "long" -> LongType()
            "float" -> FloatType()
            "double" -> DoubleType()
            "boolean" -> BooleanType()
            "string" -> StringType()
            else -> {
                throw Error("unsupported type")
            }
        }

        val clazz = result.javaClass

        // check arguments

        for ( field in node.fieldNames())
            if ( field != "type"){
                val node = node[field]

                val method = clazz.declaredMethods.first() { method -> method.name == field}!!

                val parameter = method.parameterTypes[0] // for now

                val arg = when ( parameter ) {
                    Short::class.javaObjectType -> node.asInt().toShort()
                    Integer::class.javaObjectType -> node.asInt()
                    Int::class.javaObjectType -> node.asInt()
                    Long::class.javaObjectType -> node.asLong()
                    Float::class.javaObjectType -> node.asDouble().toFloat()
                    Double::class.javaObjectType -> node.asDouble()
                    String::class.javaObjectType -> node.asText()
                    Boolean::class.javaObjectType -> node.asBoolean()

                    else -> {
                        throw Error("unsupported type")
                    }
                }

                method.invoke(result, arg)
            }

        // done

        return result
    }
}