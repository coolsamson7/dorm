package org.sirius.dorm.json

import org.sirius.dorm.`object`.DataObject
import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.PropertyDescriptor
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
import org.sirius.dorm.transaction.Status
import java.io.IOException
import java.util.*
import java.util.concurrent.ConcurrentHashMap

// read

typealias JSONPropertyReader = (node: JsonNode, obj: DataObject) -> Unit

class JSONReader(private val objectDescriptor: ObjectDescriptor) {
    // instance data

    private val readers = objectDescriptor.properties.map { property -> reader4(property) }.toTypedArray()

    // public

    fun read(node: JsonNode) : DataObject {
        val obj = objectDescriptor.create(Status.MANAGED)

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
                        throw Error("unsupported type ${property.asAttribute().type.baseType}")
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

typealias JSONPropertyWriter = (serializer : ObjectSerializer, jsonGenerator: JsonGenerator, obj: DataObject,  context: JSONWriteContext) -> Unit

class JSONWriteContext {
    // instance data

    var currentPath = ""
    val path : Queue<String> = LinkedList()
    val objects = IdentityHashMap<DataObject, String>()

    init {
        pushPath("/")
    }

    // public

    fun pushPath(path: String) {
        val nextPath = currentPath + path

        this.path.add(nextPath)

        currentPath = nextPath
    }

    fun popPath() {
        path.remove()
        currentPath = path.peek()
    }

    fun isKnown(obj: DataObject) : Boolean {
        return objects.containsKey(obj)
    }

    fun ref(obj: DataObject) : String {
        return objects[obj]!!
    }


    fun remember(obj: DataObject, path: String) {
        objects[obj] = path
    }
}

class JSONWriter(private val objectDescriptor: ObjectDescriptor) {
    // instance data

    private val writers = objectDescriptor.properties.map { property -> writer4(property) }.toTypedArray()

    // public

    fun write(serializer : ObjectSerializer, obj: DataObject, jsonGenerator: JsonGenerator, context: JSONWriteContext) {
        //jsonGenerator.writeStartObject()
        if (context.isKnown(obj)) {
            jsonGenerator.writeStringField("@ref", context.ref(obj))
        }
        else {
            context.remember(obj, context.currentPath)

            jsonGenerator.writeStringField("@type", objectDescriptor.name)

            for ( writer in writers)
                writer(serializer, jsonGenerator, obj, context)
        }

        //jsonGenerator.writeEndObject()
    }

    // companion

    companion object {
        fun writer4(property: PropertyDescriptor<Any>): JSONPropertyWriter {
            if ( property.isAttribute())
                return when (property.asAttribute().type.baseType) {
                    Boolean::class.javaObjectType -> {serializer, jsonGenerator, obj, context -> jsonGenerator.writeBooleanField(property.name, obj.value<Boolean>(property.index)!!) }

                    String::class.javaObjectType -> {serializer, jsonGenerator, obj, context ->
                        jsonGenerator.writeStringField(property.name, obj.value(property.index))
                    }

                    Short::class.javaObjectType -> {serializer, jsonGenerator, obj, context ->
                        jsonGenerator.writeNumberField(property.name, obj.value<Short>(property.index)!!)
                    }

                    Int::class.javaObjectType -> {serializer, jsonGenerator, obj, context ->
                        jsonGenerator.writeNumberField(property.name, obj.value<Int>(property.index)!!)
                    }

                    Integer::class.javaObjectType -> {serializer, jsonGenerator, obj, context ->
                        jsonGenerator.writeNumberField(property.name, obj.value<Integer>(property.index)!!.toInt())
                    }

                    Long::class.javaObjectType -> {serializer, jsonGenerator, obj, context ->
                        jsonGenerator.writeNumberField(property.name, obj.value<Long>(property.index)!!)
                    }

                    Float::class.javaObjectType -> {serializer, jsonGenerator, obj, context ->
                        jsonGenerator.writeNumberField(property.name, obj.value<Float>(property.index)!!)
                    }

                    Double::class.javaObjectType -> {serializer, jsonGenerator, obj, context ->
                        jsonGenerator.writeNumberField(property.name, obj.value<Double>(property.index)!!)
                    }

                    else -> {
                        throw Error("unsupported type ${property.asAttribute().type.baseType}")
                    }
                }
                else {
                    if ( property.asRelation().multiplicity.mutliValued) {
                        return { serializer, jsonGenerator, obj, context ->
                            val value = obj.relation(property.index)

                            jsonGenerator.writeArrayFieldStart(property.name)

                            var i = 0
                            for ( element in value) {
                                try {
                                    context.pushPath(property.name + "[${i++}]")
                                    jsonGenerator.writeStartObject()
                                    serializer.writer4(element.type).write(serializer, element, jsonGenerator, context)
                                    jsonGenerator.writeEndObject()
                                }
                                finally {
                                    context.popPath()
                                }
                            }

                            jsonGenerator.writeEndArray()
                        }
                    }
                    else // single valued
                        return { serializer, jsonGenerator, obj, context ->
                            val value = obj.value<DataObject>(property.index)

                            if (value !== null) {
                                context.pushPath(property.name)
                                try {
                                    jsonGenerator.writeObjectFieldStart(property.name)

                                    serializer.writer4(value.type).write(serializer, value, jsonGenerator, context)

                                    jsonGenerator.writeEndObject()
                                }
                                finally {
                                    context.popPath()
                                }
                            }
                            else
                                jsonGenerator.writeNullField(property.name)
                        }
                }
        }
    }
}

open class ObjectSerializer : StdSerializer<DataObject>(DataObject::class.java) {
    // instance data

   private val writer = ConcurrentHashMap<String, JSONWriter>()

    // public

    fun writer4(objectDescriptor: ObjectDescriptor) : JSONWriter {
        return writer.getOrPut(objectDescriptor.name) { -> JSONWriter(objectDescriptor) }
    }

    // override

    @Throws(IOException::class)
    override fun serialize(obj: DataObject, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) {
        jsonGenerator.writeStartObject()

        writer4(obj.type).write(this, obj, jsonGenerator, JSONWriteContext())

        jsonGenerator.writeEndObject()
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