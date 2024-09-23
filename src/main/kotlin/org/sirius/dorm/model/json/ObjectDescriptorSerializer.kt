package org.sirius.dorm.model.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.sirius.dorm.model.ObjectDescriptor
import java.io.IOException

open class ObjectDescriptorSerializer : StdSerializer<ObjectDescriptor>(ObjectDescriptor::class.java) {
    // override

    @Throws(IOException::class)
    override fun serialize(obj: ObjectDescriptor, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) {
        jsonGenerator.writeStartObject()

        jsonGenerator.writeStringField("name", obj.name)
        jsonGenerator.writeObjectFieldStart("properties")

        for ( property in obj.properties) {
            jsonGenerator.writeObjectFieldStart(property.name)

            // either attribute or relation

            if ( property.isAttribute()) {
                jsonGenerator.writeObjectField("type", property.asAttribute().type)
            }
            else {
                jsonGenerator.writeStringField("target", property.asRelation().target)
                jsonGenerator.writeStringField("inverse", property.asRelation().inverse)
                jsonGenerator.writeObjectField("multiplicity", property.asRelation().multiplicity.name)

                property.asRelation()
            }

            jsonGenerator.writeEndObject()
        }

        jsonGenerator.writeEndObject()

        jsonGenerator.writeEndObject()
    }
}