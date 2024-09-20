package com.quasar.dorm.model.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.quasar.dorm.model.*
import com.quasar.dorm.type.Type

open class ObjectDescriptorDeserializer() : StdDeserializer<ObjectDescriptor>(ObjectDescriptor::class.java) {
    // override

    override fun deserialize(jsonParser: JsonParser, context: DeserializationContext): ObjectDescriptor {
        val node: JsonNode = jsonParser.codec.readTree(jsonParser)
        val name = node.get("name").asText()

        val properties = ArrayList<PropertyDescriptor<Any>>()

        val propertyNode = node.get("properties")

        for ( propertyName in propertyNode.fieldNames()) {
            val property = propertyNode.get(propertyName)

            // either attribute or relation

            if ( property["type"] !== null) {
                val type = jsonParser.getCodec().treeToValue(property.get("type"), Type::class.java)

                properties.add(AttributeDescriptor(propertyName, type as Type<Any>, propertyName == "id"))
            }
            else {
                val multiplicity = Multiplicity.valueOf( property["target"].asText())
                properties.add(RelationDescriptor(propertyName, property["target"].asText(), multiplicity))
            }
        }

        // done

        return ObjectDescriptor(name, properties.toTypedArray())
    }
}