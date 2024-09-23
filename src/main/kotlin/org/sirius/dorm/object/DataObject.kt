package org.sirius.dorm.`object`
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.PropertyDescriptor
import org.sirius.dorm.persistence.entity.EntityEntity
import org.sirius.dorm.transaction.ObjectState


class DataObject(val type: ObjectDescriptor, var state : ObjectState?) {
    // instance data

    var entity: EntityEntity? = null
    val values  = type.createValues(this)

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

    fun <T:Any> value(name: String) : T {
        return values[property(name).index].get(objectManager) as T
    }

    fun <T: Relation>relation(name: String) : T {
        return values[property(name).index] as T
    }

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

    // override Object

    override fun equals(other: Any?): Boolean {
        if ( other is DataObject)
            return id == other.id && this.type === other.type
        else
            return false
    }

    override fun hashCode(): Int {
        return type.hashCode() + id.hashCode()
    }

    companion object {
        val NONE = DataObject(ObjectDescriptor("none", arrayOf()), null)
    }
}