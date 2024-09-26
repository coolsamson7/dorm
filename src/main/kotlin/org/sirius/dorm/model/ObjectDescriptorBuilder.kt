package org.sirius.dorm.model
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.common.type.Type
import org.sirius.common.type.base.long
import org.sirius.dorm.ObjectManager


abstract class PropertyBuilder() {
    // instance data

    protected var name = ""

    // protected

    abstract fun build() : PropertyDescriptor<Any>
}
class AttributeBuilder() : PropertyBuilder() {
    // instance data

    private var primaryKey = false
    private var type: Type<*>? = null

    // fluent

    fun name(name: String) : AttributeBuilder {
        this.name = name

        return this
    }

    fun type(type: Type<*>) : AttributeBuilder {
        this.type = type

        return this
    }

    fun primaryKey() : AttributeBuilder {
        this.primaryKey = true

        return this
    }

    // public

    override fun build() : AttributeDescriptor<Any> {
        // done

        return AttributeDescriptor(name, type!!, primaryKey)
    }
}

fun attribute(name: String) : AttributeBuilder {
    return AttributeBuilder().name(name)
}

class RelationBuilder() : PropertyBuilder() {
    // instance data

    private var target = ""
    private var multiplicity : Multiplicity? = null
    private var cascade : Cascade? = null
    private var inverse : String? = null
    private var owner = false

    // fluent

    fun name(name: String) : RelationBuilder {
        this.name = name

        return this
    }

    fun target(target: String) : RelationBuilder {
        this.target = target

        return this
    }

    fun inverse(inverse: String) : RelationBuilder {
        this.inverse = inverse

        return this
    }

    fun multiplicity(multiplicity: Multiplicity) : RelationBuilder {
        this.multiplicity = multiplicity

        return this
    }

    fun cascade(cascade: Cascade) : RelationBuilder {
        this.cascade = cascade

        return this
    }

    fun owner() : RelationBuilder {
        this.owner = true

        return this
    }

    // build

    override fun build() : RelationDescriptor<Any> {
        return RelationDescriptor(name, target, multiplicity!!, cascade, inverse, owner)
    }
}

fun relation(name: String) : RelationBuilder {
    return RelationBuilder().name(name)
}

class ObjectDescriptorBuilder(val manager: ObjectManager, val name: String) {
    // instance data

    private val properties = ArrayList<PropertyDescriptor<Any>>()

    init {
        property(id)
    }

    // fluent

    fun property(propertyBuilder: PropertyBuilder) : ObjectDescriptorBuilder {
        properties.add(propertyBuilder.build())

        return this
    }

    // public

    fun register() {
        manager.register(ObjectDescriptor(name, properties.toTypedArray()))
    }

    companion object {
        val id = attribute("id").type(long()).primaryKey()
    }
}