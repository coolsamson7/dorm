package com.quasar.dorm.type
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.module.SimpleSerializers
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.quasar.dorm.type.base.*
import java.io.IOException


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
                    Short::class.java -> node.asInt().toShort()
                    Integer::class.java -> node.asInt()
                    Int::class.java -> node.asInt()
                    Long::class.java -> node.asLong()
                    Float::class.java -> node.asDouble().toFloat()
                    Double::class.java -> node.asDouble()
                    String::class.java -> node.asText()
                    Boolean::class.java -> node.asBoolean()

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

open class TypeSerializer : StdSerializer<Type<*>>(Type::class.java) {
    // instance data

    // private

    // override

    @Throws(IOException::class)
    override fun serialize(obj: Type<*>, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) {
        jsonGenerator.writeStartObject()

        for ( test in obj.tests) {
            if ( test.parameter != null) {
                when ( test.parameter.javaClass) {
                    Short::class.java -> jsonGenerator.writeNumberField(test.name, (test.parameter as Short).toInt())
                    Integer::class.java -> jsonGenerator.writeNumberField(test.name, (test.parameter as Integer).toInt())
                    Long::class.java -> jsonGenerator.writeNumberField(test.name, (test.parameter as Long).toInt())
                    Float::class.java -> jsonGenerator.writeNumberField(test.name, (test.parameter as Float).toFloat())
                    Double::class.java -> jsonGenerator.writeNumberField(test.name, (test.parameter as Double).toDouble())
                    Boolean::class.java -> jsonGenerator.writeBooleanField(test.name, test.parameter as Boolean)
                    String::class.java -> jsonGenerator.writeStringField(test.name, (test.parameter as String))
                }
            }
            else {
                jsonGenerator.writeBooleanField(test.name, true)
            }
        }

        jsonGenerator.writeEndObject()
    }
}

class TypeModule: SimpleModule() {
    // override

    override fun getModuleName(): String = this.javaClass.simpleName

    override fun setupModule(context: SetupContext) {
        // serializer

        val serializers = SimpleSerializers()

        serializers.addSerializer(Type::class.java, TypeSerializer())

        context.addSerializers(serializers)

        // deserializer

        val deserializers = SimpleDeserializers()

        deserializers.addDeserializer(Type::class.java, TypeDeserializer())

        context.addDeserializers(deserializers)
    }
}