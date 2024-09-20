package com.quasar.dorm.json

import com.quasar.dorm.DataObject
import com.quasar.dorm.ObjectManager
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
            if ( property.isAttribute())
                return when (property.asAttribute().type.baseType) {
                    Boolean::class.javaObjectType -> { node: JsonNode, obj: DataObject ->
                        obj[property.name] = node.get(property.name).asBoolean()
                    }

                    String::class.javaObjectType -> { node: JsonNode, obj: DataObject ->
                        obj[property.name] = node.get(property.name).asText()
                    }

                    Short::class.javaObjectType -> { node: JsonNode, obj: DataObject ->
                        obj[property.name] = node.get(property.name).asInt().toShort()
                    }

                    Int::class.javaObjectType -> { node: JsonNode, obj: DataObject ->
                        obj[property.name] = node.get(property.name).asInt()
                    }

                    Integer::class.javaObjectType -> { node: JsonNode, obj: DataObject ->
                        obj[property.name] = node.get(property.name).asInt()
                    }

                    Long::class.javaObjectType -> { node: JsonNode, obj: DataObject ->
                        obj[property.name] = node.get(property.name).asInt().toLong()
                    }

                    Float::class.javaObjectType -> { node: JsonNode, obj: DataObject ->
                        obj[property.name] = node.get(property.name).asDouble().toFloat()
                    }

                    Double::class.javaObjectType -> { node: JsonNode, obj: DataObject ->
                        obj[property.name] = node.get(property.name).asDouble()
                    }

                    else -> {
                        throw Error("unsupported type")
                    }
                }
            else return { node: JsonNode, obj: DataObject -> // TODO: relation
                //TODO val id = node.get(property.name).asInt()

                //obj[property.name] = node.get(property.name).asDouble()
            }
        }
    }
}

open class ObjectDeserializer() : StdDeserializer<DataObject>(DataObject::class.java) {
    // instance data

    private val readers = ConcurrentHashMap<String, JSONReader>()

    // private

    private fun reader4(objectDescriptor: ObjectDescriptor) : JSONReader {
        return readers.getOrPut(objectDescriptor.name) { -> JSONReader(objectDescriptor) }
    }

    // override

    override fun deserialize(jsonParser: JsonParser, context: DeserializationContext): DataObject {
        val node: JsonNode = jsonParser.codec.readTree(jsonParser)

        val type = node.get("@type").asText()

        return reader4(ObjectManager.instance.getDescriptor(type)).read(node)
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
            if ( property.isAttribute())
                return when (property.asAttribute().type.baseType) {
                    Boolean::class.javaObjectType -> {jsonGenerator, obj -> jsonGenerator.writeBooleanField(property.name, obj.value<Boolean>(property.index)) }

                    String::class.javaObjectType -> {jsonGenerator, obj ->
                        jsonGenerator.writeStringField(property.name, obj.value<String>(property.index))
                    }

                    Short::class.javaObjectType -> {jsonGenerator, obj ->
                        jsonGenerator.writeNumberField(property.name, obj.value<Short>(property.index))
                    }

                    Int::class.javaObjectType -> {jsonGenerator, obj ->
                        jsonGenerator.writeNumberField(property.name, obj.value<Int>(property.index))
                    }

                    Integer::class.javaObjectType -> {jsonGenerator, obj ->
                        jsonGenerator.writeNumberField(property.name, obj.value<Integer>(property.index).toInt())
                    }

                    Long::class.javaObjectType -> {jsonGenerator, obj ->
                        jsonGenerator.writeNumberField(property.name, obj.value<Long>(property.index))
                    }

                    Float::class.javaObjectType -> {jsonGenerator, obj ->
                        jsonGenerator.writeNumberField(property.name, obj.value<Float>(property.index))
                    }

                    Double::class.javaObjectType -> {jsonGenerator, obj ->
                        jsonGenerator.writeNumberField(property.name, obj.value<Double>(property.index))
                    }

                    else -> {
                        throw Error("unsupported type")
                    }
                }
                else {
                    if ( property.asRelation().multiplicity.mutliValued) {
                        return { jsonGenerator, obj ->
                            // TODO
                        }
                    }
                    else // single valued
                        return { jsonGenerator, obj -> // TODO RELATION
                            //if (obj.value<DataObject>(property.index) !== null)
                            //    jsonGenerator.writeNullField(property.name)//jsonGenerator.writeNumberField(property.name, (obj.values[property.index] as DataObject).id)
                            //else
                                jsonGenerator.writeNullField(property.name)
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