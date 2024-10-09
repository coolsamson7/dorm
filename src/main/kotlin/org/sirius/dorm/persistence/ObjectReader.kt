package org.sirius.dorm.persistence
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.ObjectManager
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.model.PropertyDescriptor
import org.sirius.dorm.`object`.DataObject
import org.sirius.dorm.persistence.entity.EntityEntity
import org.sirius.dorm.persistence.entity.PropertyEntity


typealias PropertyReader = (obj: DataObject, property: PropertyEntity, entity: EntityEntity) -> Unit

class ObjectReader(descriptor: ObjectDescriptor, objectManager: ObjectManager) {
    // instance data

    private val reader: Array<PropertyReader> =
        descriptor.properties.map { property -> reader4(property, objectManager) }.toTypedArray()

    // public

    fun readEntity(obj: DataObject, entity: EntityEntity) {
        reader[0](obj, entity.properties[0], entity) // id
        reader[1](obj, entity.properties[0], entity) // versionCounter
        reader[2](obj, entity.properties[0], entity) // status
    }

    fun read(obj: DataObject, propertyDescriptor: PropertyDescriptor<Any>, property: PropertyEntity, entity: EntityEntity) {
        obj.values[propertyDescriptor.index].property = property

        reader[propertyDescriptor.index](obj, property, entity)
    }

    // companion

    companion object {
        fun valueReader(clazz: Class<out Any>) :  (attribute: PropertyEntity, entity: EntityEntity) -> Any {
            return when (clazz) {
                Boolean::class.javaObjectType -> { attribute: PropertyEntity, entity: EntityEntity -> attribute.intValue == 1 }

                Short::class.javaObjectType -> { attribute: PropertyEntity, entity: EntityEntity-> attribute.intValue.toShort() }

                Integer::class.javaObjectType -> { attribute: PropertyEntity, entity: EntityEntity -> attribute.intValue }

                Long::class.javaObjectType -> { attribute: PropertyEntity, entity: EntityEntity -> attribute.intValue.toLong() }

                Float::class.javaObjectType -> { attribute: PropertyEntity, entity: EntityEntity -> attribute.doubleValue.toFloat() }

                Double::class.javaObjectType -> { attribute: PropertyEntity, entity: EntityEntity -> attribute.doubleValue }

                String::class.javaObjectType -> { attribute: PropertyEntity, entity: EntityEntity -> attribute.stringValue }

                else -> throw Error("unsupported type")
            }
        }

        fun reader4(property: PropertyDescriptor<Any>, objectManager: ObjectManager): PropertyReader {
            if ( !property.isAttribute()) {
                return  { obj: DataObject, attribute: PropertyEntity, entity: EntityEntity ->
                   // relations are fetched lazily
                }
            }
            else return when (property.name) {
                "id" ->  { obj: DataObject, attribute: PropertyEntity, entity: EntityEntity ->
                    obj.values[property.index].init(property, entity.id, objectManager)
                }

                "versionCounter" ->  { obj: DataObject, attribute: PropertyEntity, entity: EntityEntity ->
                    obj.values[property.index].init(property, entity.versionCounter, objectManager)
                }

                "status" ->  { obj: DataObject, attribute: PropertyEntity, entity: EntityEntity ->
                    obj.values[property.index].init(property, entity.status, objectManager)
                }

                else ->
                    when (property.asAttribute().baseType()) {
                        Boolean::class.javaObjectType -> { obj: DataObject, attribute: PropertyEntity, entity: EntityEntity ->
                            obj.values[property.index].init(property, attribute.intValue == 1, objectManager)
                        }

                        String::class.javaObjectType -> { obj: DataObject, attribute: PropertyEntity, entity: EntityEntity ->
                            obj.values[property.index].init(property, attribute.stringValue, objectManager)
                        }

                        Short::class.javaObjectType -> { obj: DataObject, attribute: PropertyEntity, entity: EntityEntity ->
                            obj.values[property.index].init(property, attribute.intValue.toShort(), objectManager)
                        }

                        Integer::class.javaObjectType -> { obj: DataObject, attribute: PropertyEntity, entity: EntityEntity ->
                            obj.values[property.index].init(property, attribute.intValue, objectManager)
                        }

                        Long::class.javaObjectType -> { obj: DataObject, attribute: PropertyEntity, entity: EntityEntity ->
                            obj.values[property.index].init(property, attribute.intValue.toLong(), objectManager)
                        }

                        Float::class.javaObjectType -> { obj: DataObject, attribute: PropertyEntity, entity: EntityEntity ->
                            obj.values[property.index].init(property, attribute.doubleValue.toFloat(), objectManager)
                        }

                        Double::class.javaObjectType -> { obj: DataObject, attribute: PropertyEntity, entity: EntityEntity ->
                            obj.values[property.index].init(property, attribute.doubleValue, objectManager)
                        }

                        else -> { _: DataObject, _: PropertyEntity, _: EntityEntity ->
                            throw Error("unsupported type")
                        }
                    }
            }
        }
    }
}