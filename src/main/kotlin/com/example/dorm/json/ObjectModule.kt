package com.example.dorm.json

import com.example.dorm.DataObject
import com.example.dorm.ObjectManager
import com.example.dorm.model.ObjectDescriptor
import com.example.dorm.model.PropertyDescriptor
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
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

// read

typealias JSONPropertyReader = (node: JsonNode, obj: DataObject) -> Unit

class JSONReader(private val objectDescriptor: ObjectDescriptor) {
    // instance data

    private val readers = objectDescriptor.properties.map { property -> reader4(property) }.toTypedArray()

    // public

    fun read(node: JsonNode) : DataObject {
        val obj = objectDescriptor.create()

        for ( reader in readers)
            reader(node, obj)

        return obj
    }

    // companion

    companion object {
        fun reader4(property: PropertyDescriptor<Any>): JSONPropertyReader {
            return when (property.type.baseType) {
                Boolean::class.java -> { node: JsonNode, obj: DataObject ->
                    obj[property.name] = node.get(property.name).asBoolean()
                }

                String::class.java -> { node: JsonNode, obj: DataObject ->
                    obj[property.name] = node.get(property.name).asText()
                }

                Short::class.java -> { node: JsonNode, obj: DataObject ->
                    obj[property.name] = node.get(property.name).asInt().toShort()
                }

                Int::class.java -> { node: JsonNode, obj: DataObject ->
                    obj[property.name] = node.get(property.name).asInt()
                }

                Integer::class.java -> { node: JsonNode, obj: DataObject ->
                    obj[property.name] = node.get(property.name).asInt()
                }

                Long::class.java -> { node: JsonNode, obj: DataObject ->
                    obj[property.name] = node.get(property.name).asInt().toLong()
                }

                Float::class.java -> { node: JsonNode, obj: DataObject ->
                    obj[property.name] = node.get(property.name).asDouble().toFloat()
                }

                Double::class.java -> { node: JsonNode, obj: DataObject ->
                    obj[property.name] = node.get(property.name).asDouble()
                }

                else -> {
                    throw Error("unsupported type")
                }
            }
        }
    }
}

open class ObjectDeserializer() : StdDeserializer<DataObject>(DataObject::class.java) {
    // instance data

    val readers = ConcurrentHashMap<String, JSONReader>()

    // private

    private fun reader4(objectDescriptor: ObjectDescriptor) : JSONReader {
        return readers.getOrPut(objectDescriptor.name) { -> JSONReader(objectDescriptor) }
    }

    // override

    override fun deserialize(jsonParser: JsonParser, context: DeserializationContext): DataObject {
        val node: JsonNode = jsonParser.codec.readTree(jsonParser)

        val type = node.get("@type").asText()

        return reader4(ObjectManager.instance.get(type)).read(node)
    }
}

// write

typealias JSONPropertyWriter = (jsonGenerator: JsonGenerator, obj: DataObject) -> Unit

class JSONWriter(private val objectDescriptor: ObjectDescriptor) {
    // instance data

    private val writers = objectDescriptor.properties.map { property -> writer4(property) }.toTypedArray()

    // public

    fun write(obj: DataObject, jsonGenerator: JsonGenerator) {
        jsonGenerator.writeStartObject()

        jsonGenerator.writeStringField("@type", objectDescriptor.name)

        for ( writer in writers)
            writer(jsonGenerator, obj)

        jsonGenerator.writeEndObject()
    }

    // companion

    companion object {
        fun writer4(property: PropertyDescriptor<Any>): JSONPropertyWriter {
            return when (property.type.baseType) {
                Boolean::class.java -> {jsonGenerator, obj -> jsonGenerator.writeBooleanField(property.name, obj.values[property.index] as Boolean) }

                String::class.java -> {jsonGenerator, obj ->
                    jsonGenerator.writeStringField(property.name, obj.value(property.index, String::class.java)) // do we like that function?
                }

                Short::class.java -> {jsonGenerator, obj ->
                    jsonGenerator.writeNumberField(property.name, obj.values[property.index] as Short)
                }

                Int::class.java -> {jsonGenerator, obj ->
                    jsonGenerator.writeNumberField(property.name, obj.values[property.index] as Int)
                }

                Integer::class.java -> {jsonGenerator, obj ->
                    jsonGenerator.writeNumberField(property.name, obj.values[property.index] as Int)
                }

                Long::class.java -> {jsonGenerator, obj ->
                    jsonGenerator.writeNumberField(property.name, obj.values[property.index] as Long)
                }

                Float::class.java -> {jsonGenerator, obj ->
                    jsonGenerator.writeNumberField(property.name, obj.values[property.index] as Float)
                }

                Double::class.java -> {jsonGenerator, obj ->
                    jsonGenerator.writeNumberField(property.name, obj.values[property.index] as Double)
                }

                else -> {
                    throw Error("unsupported type")
                }
            }
        }
    }
}

open class ObjectSerializer : StdSerializer<DataObject>(DataObject::class.java) {
    // instance data

   private val writer = ConcurrentHashMap<String, JSONWriter>()

    // private

    private fun writer4(objectDescriptor: ObjectDescriptor) : JSONWriter {
        return writer.getOrPut(objectDescriptor.name) { -> JSONWriter(objectDescriptor) }
    }

    // override

    @Throws(IOException::class)
    override fun serialize(obj: DataObject, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) {
        writer4(obj.type).write(obj, jsonGenerator)
    }
}

class ObjectModule: SimpleModule() {
    // override

    override fun getModuleName(): String = this.javaClass.simpleName

    override fun setupModule(context: SetupContext) {
        // serializer

        val serializers = SimpleSerializers()

        serializers.addSerializer(DataObject::class.java, ObjectSerializer())

        context.addSerializers(serializers)

        // deserializer

        val deserializers = SimpleDeserializers()

        deserializers.addDeserializer(DataObject::class.java, ObjectDeserializer())

        context.addDeserializers(deserializers)
    }
}