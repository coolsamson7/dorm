package org.sirius.dorm.`object`
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.Cascade
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.PropertyDescriptor
import org.sirius.dorm.persistence.entity.EntityEntity
import org.sirius.dorm.transaction.ObjectState
import org.sirius.dorm.transaction.Status


class DataObject(val type: ObjectDescriptor, status: Status, var state : ObjectState?) {
    // instance data

    var entity: EntityEntity? = null
    val values  = type.createValues(this, status)

    // public

    fun delete() {
        state?.status = Status.DELETED

        var i = 0
        for (property in type.properties) {
            if (!property.isAttribute()) {
                if (property.asRelation().cascade == Cascade.DELETE) {
                    if ((values[i] as Relation).isLoaded())
                        (values[i] as Relation).deleted()
                }
            }

            i++
        }
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

    fun <T:Any> value(index: Int) : T {
        return values[index].get(objectManager) as T
    }

    fun <T:Any> value(name: String) : T {
        return values[property(name).index].get(objectManager) as T
    }

    fun <T: Relation>relation(name: String) : T {
        return (values[property(name).index] as Relation).get(objectManager) as T
    }

    fun <T: Relation>relation(index: Int) : T {
        return (values[index] as Relation).get(objectManager) as T
    }

    operator fun get(name: String) : Any? {
        return values[property(name).index].get(objectManager)
    }

    operator fun set(name: String, value: Any?) {
        val property = property(name)

        // take snapshot in any case

        if ( state !== null )
            state!!.takeSnapshot(this)

        // set raw value

        values[property.index].set(property, value, objectManager)
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
        val NONE = DataObject(ObjectDescriptor("none", arrayOf()), Status.CREATED, null)
    }
}