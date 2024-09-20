package com.quasar.dorm.model.json
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.module.SimpleSerializers
import com.quasar.dorm.model.*
import com.quasar.dorm.model.json.ObjectDescriptorDeserializer
import com.quasar.dorm.model.json.ObjectDescriptorSerializer

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