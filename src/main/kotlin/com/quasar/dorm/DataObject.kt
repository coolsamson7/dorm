package com.quasar.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import com.quasar.dorm.model.ObjectDescriptor
import com.quasar.dorm.model.PropertyDescriptor
import com.quasar.dorm.persistence.entity.AttributeEntity
import com.quasar.dorm.persistence.entity.EntityEntity
import com.quasar.dorm.transaction.ObjectState

abstract class Property(var property: AttributeEntity?) {
    abstract fun get(objectManager: ObjectManager) : Any?
    abstract fun set(propertyDescriptor: PropertyDescriptor<Any>, value: Any?) : Boolean

    // snapshot stuff
    abstract fun save(): Any;

    abstract fun restore(state: Any);

    open fun isDirty(snapshot: Any) : Boolean {
        return false
    }

    open fun flush() {}
}

abstract class Relation(property: AttributeEntity?) : Property(property) {
}

class SingleValuedRelation(property: AttributeEntity?, val targetDescriptor: ObjectDescriptor) : Relation(property) {
    // instance data

    var target: DataObject? = DataObject.NONE

    // private

    private fun isLoaded() : Boolean {
        return target !== DataObject.NONE
    }

    // override

    override fun flush() {
        if ( isLoaded()) {
            property!!.relations.clear()
            if ( target !== null) {
                property!!.relations.add(target!!.entity!!)
            }
        }
    }
    override fun get(objectManager: ObjectManager) : Any? {
        if ( !isLoaded() && property !== null) {
            if ( property!!.relations.size == 1)
                target = objectManager.findById(targetDescriptor, property!!.relations.first().id)
            else
                target = null
        }

        return target
    }

    override fun set(propertyDescriptor: PropertyDescriptor<Any>, value: Any?) : Boolean {
        this.target = value as DataObject?

        return true
    }

    override fun save(): Any {
        return this
    }

    override fun restore(state: Any) {
    }
}

class MultiValuedRelation(property: AttributeEntity?, targetDescriptor: ObjectDescriptor) : Relation(property) {
    override fun get(objectManager: ObjectManager) : Any? {
        return null // TODO RELATION
    }

    override fun set(propertyDescriptor: PropertyDescriptor<Any>, value: Any?) : Boolean  {
        // TODO RELATIONthis.value = value!!
        return true
    }

    override fun save(): Any {
        return this
    }

    override fun restore(state: Any) {}
}

class Attribute(property: AttributeEntity?, var value: Any) : Property(property) {
    override fun get(objectManager: ObjectManager) : Any? {
        return value
    }

    override fun set(propertyDescriptor: PropertyDescriptor<Any>, value: Any?) : Boolean {
        propertyDescriptor.validate(value)

        if ( value != this.value ) {
            this.value = value!!

            return true
        }
        else return false
    }

    override fun save(): Any {
        return value
    }

    override fun restore(state: Any) {
        // TODO
    }

    override fun isDirty(snapshot: Any) : Boolean {
        return value != snapshot
    }
}


class DataObject(val type: ObjectDescriptor, var state : ObjectState?, val values: Array<Property>) {
    // instance data

    var entity: EntityEntity? = null

    // public

    var objectManager: ObjectManager
        get() = type.objectManager!!
        set(value) { }

    fun property(property: String) : PropertyDescriptor<Any> {
        return type.property(property)
    }

    var id: Int
        get() = values[0].get(objectManager) as Int
        set(value) { values[0].set(type.properties[0], value) }


    fun snapshot() : List<Any> {
        return values.map { value -> value.save() }
    }

    // public operators

    fun <T:Any> value(index: Int) : T {
        return values[index].get(objectManager) as T
    }

    /*fun reference(index: Int) : SingleValuedRelation {
        return values[index] as SingleValuedRelation
    }

    fun relation(index: Int) : MultiValuedRelation {
        return values[index] as MultiValuedRelation
    }*/

    operator fun get(name: String) : Any? {
        return values[property(name).index].get(objectManager)
    }

    operator fun set(name: String, value: Any) {
        val property = property(name)

        // take snapshot in any case

        if ( state !== null )
            state!!.takeSnapshot(this)

        // set raw value

        values[property.index].set(property, value)
    }

    companion object {
        val NONE = DataObject(ObjectDescriptor("none", arrayOf()), null, arrayOf())
    }
}