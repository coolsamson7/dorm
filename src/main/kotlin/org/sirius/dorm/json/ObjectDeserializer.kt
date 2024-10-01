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
import java.util.*
import java.util.concurrent.ConcurrentHashMap


typealias JSONPropertyReader = (node: JsonNode, obj: DataObject, deserializer : ObjectDeserializer, context : JSONReaderContext) -> Unit

class JSONReaderContext {
    // instance data

    var currentPath = ""
    val path : Queue<String> = LinkedList()
    val objects = HashMap<String, DataObject>()

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

    fun obj(ref: String) : DataObject {
        return objects[ref]!!
    }

    fun remember( path: String, obj: DataObject) {
        objects[path] = obj
    }
}
class JSONReader(private val objectDescriptor: ObjectDescriptor) {
    // instance data

    private val readers = objectDescriptor.properties.map { property -> reader4(property) }.toTypedArray()

    // public

    fun read(node: JsonNode, deserializer : ObjectDeserializer, context : JSONReaderContext) : DataObject {
        if ( node.has("@ref")) {
            return context.obj(node.get("@ref").asText())
        }
        else {
            val obj = objectDescriptor.create(Status.MANAGED)

            context.remember(context.currentPath, obj)

            for ( reader in readers)
                reader(node, obj, deserializer, context)

            return obj
        }
    }

    // companion

    companion object {
        fun reader4(property: PropertyDescriptor<Any>): JSONPropertyReader {
            if ( property.isAttribute())
                return when (property.asAttribute().type.baseType) {
                    Boolean::class.javaObjectType -> { node, obj, deserializer, context ->
                        obj[property.name] = node.get(property.name).asBoolean()
                    }

                    String::class.javaObjectType -> { node, obj, deserializer, context ->
                        obj[property.name] = node.get(property.name).asText()
                    }

                    Short::class.javaObjectType -> { node, obj, deserializer, context ->
                        obj[property.name] = node.get(property.name).asInt().toShort()
                    }

                    Int::class.javaObjectType -> { node, obj, deserializer, context ->
                        obj[property.name] = node.get(property.name).asInt()
                    }

                    Integer::class.javaObjectType -> { node, obj, deserializer, context ->
                        obj[property.name] = node.get(property.name).asInt()
                    }

                    Long::class.javaObjectType -> { node, obj, deserializer, context ->
                        obj[property.name] = node.get(property.name).asInt().toLong()
                    }

                    Float::class.javaObjectType -> { node, obj, deserializer, context ->
                        obj[property.name] = node.get(property.name).asDouble().toFloat()
                    }

                    Double::class.javaObjectType -> { node, obj, deserializer, context ->
                        obj[property.name] = node.get(property.name).asDouble()
                    }

                    else -> {
                        throw Error("unsupported type ${property.asAttribute().type.baseType}")
                    }
                }
            else return { node, obj, deserializer, context ->
                val target = property.asRelation().targetDescriptor!!

                if ( property.asRelation().multiplicity.mutliValued) {
                    // multivalued

                    val array =  node.get(property.name)

                    context.pushPath(property.name)
                    try {
                        for ( i in 0..<node.size()) {
                            val element = array[i]

                            obj.relation(property.name).add(deserializer.reader4(target).read(element, deserializer, context))
                        }
                    }
                    finally {
                        context.popPath()
                    }
                }
                else {
                    // single valued

                    val child = node.get(property.name)

                    //val type = child.get("@type").asText()

                    if ( child.isNull) {
                        obj[property.name] = null
                    }
                    else {
                        context.pushPath(property.name)
                        try {
                            obj[property.name] = deserializer.reader4(target).read(child, deserializer, context)
                        }
                        finally {
                            context.popPath()
                        }
                    }
                }
            }
        }
    }
}


open class ObjectDeserializer() : StdDeserializer<DataObject>(DataObject::class.java) {
    // instance data

    private val readers = ConcurrentHashMap<String, JSONReader>()

    // public

    fun reader4(objectDescriptor: ObjectDescriptor) : JSONReader {
        return readers.getOrPut(objectDescriptor.name) { -> JSONReader(objectDescriptor) }
    }

    // override

    override fun deserialize(jsonParser: JsonParser, context: DeserializationContext): DataObject {
        val node: JsonNode = jsonParser.codec.readTree(jsonParser)

        val type = node.get("@type").asText()

        return reader4(ObjectManager.instance.getDescriptor(type)).read(node, this, JSONReaderContext())
    }
}