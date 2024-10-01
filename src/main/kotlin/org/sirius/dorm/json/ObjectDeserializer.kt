package org.sirius.dorm.json
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.PropertyDescriptor
import org.sirius.dorm.`object`.DataObject
import org.sirius.dorm.transaction.Status
import java.util.concurrent.ConcurrentHashMap


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