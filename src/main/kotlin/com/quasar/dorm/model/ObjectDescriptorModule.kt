package com.quasar.dorm.json
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.model.ObjectDescriptor
import com.quasar.dorm.model.PropertyDescriptor
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
import com.quasar.dorm.type.Type
import com.quasar.dorm.type.base.*
import java.io.IOException

open class ObjectDescriptorDeserializer() : StdDeserializer<ObjectDescriptor>(ObjectDescriptor::class.java) {
    // override

    override fun deserialize(jsonParser: JsonParser, context: DeserializationContext): ObjectDescriptor {
        val node: JsonNode = jsonParser.codec.readTree(jsonParser)
        val name = node.get("name").asText()

        val properties = ArrayList<PropertyDescriptor<Any>>()

        val propertyNode = node.get("properties")

        for ( propertyName in propertyNode.fieldNames()) {
            val property = propertyNode.get(propertyName)

            val type = jsonParser.getCodec().treeToValue(property.get("type"), Type::class.java)

            properties.add(PropertyDescriptor(propertyName, type as Type<Any>))
        }

        // done

        return ObjectDescriptor(name, properties.toTypedArray())
    }
}

open class ObjectDescriptorSerializer : StdSerializer<ObjectDescriptor>(ObjectDescriptor::class.java) {
    // instance data

    // private

    // override

    @Throws(IOException::class)
    override fun serialize(obj: ObjectDescriptor, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) {
        jsonGenerator.writeStartObject()

        jsonGenerator.writeStringField("name", obj.name)
        jsonGenerator.writeObjectFieldStart("properties")

        for ( property in obj.properties) {
            jsonGenerator.writeObjectFieldStart(property.name)
            jsonGenerator.writeObjectField("type", property.type)
            jsonGenerator.writeEndObject()
        }

        jsonGenerator.writeEndObject()

        jsonGenerator.writeEndObject()
    }
}

class ObjectDescriptorModule: SimpleModule() {
    // override

    override fun getModuleName(): String = this.javaClass.simpleName

    override fun setupModule(context: SetupContext) {
        // serializer

        val serializers = SimpleSerializers()

        serializers.addSerializer(ObjectDescriptor::class.java, ObjectDescriptorSerializer())

        context.addSerializers(serializers)

        // deserializer

        val deserializers = SimpleDeserializers()

        deserializers.addDeserializer(ObjectDescriptor::class.java, ObjectDescriptorDeserializer())

        context.addDeserializers(deserializers)
    }
}