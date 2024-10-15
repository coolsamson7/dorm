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
import org.sirius.dorm.transaction.Status


class DataObject(val type: ObjectDescriptor, vararg status: Status) {
    // instance data

    val state = ObjectState(this, *status)
    var entity: EntityEntity? = null
    val values  = type.createValues(this)

    // public

    fun delete() {
        state.set(Status.DELETED)
    }

    var objectManager: ObjectManager
        get() = type.objectManager!!
        set(_) { }

    fun property(property: String) : PropertyDescriptor<Any> {
        return type.property(property)
    }

    var id: Long
        get() = (values[0] as Attribute).value as Long
        set(value) { (values[0]as Attribute).value = value }


    fun snapshot() : List<Any> {
        return values.map { value -> value.save() }
    }

    // public operators

    fun <T:Any> value(index: Int) : T? {
        return values[index].get(objectManager)
    }

    fun <T:Any> value(name: String) : T? {
        return values[property(name).index].get(objectManager)
    }

    fun relation(name: String) : MultiValuedRelation {
        return values[property(name).index].get(objectManager)!!
    }

    fun relation(index: Int) : MultiValuedRelation {
        return values[index].get(objectManager)!!
    }

    operator fun get(name: String) : Any? {
        return values[property(name).index].get(objectManager)
    }

    operator fun set(name: String, value: Any?) {
        val property = property(name)

        state.modified()

        // set raw value

        values[property.index].set(property, value, objectManager)
    }

    // override Object

    override fun equals(other: Any?): Boolean {
        return if ( other is DataObject)
            id == other.id && this.type === other.type
        else
            false
    }

    override fun hashCode(): Int {
        return type.hashCode() + id.hashCode()
    }

    companion object {
        val NONE = DataObject(ObjectDescriptor("none", arrayOf()), Status.CREATED)
    }
}