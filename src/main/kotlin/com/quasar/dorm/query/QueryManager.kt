package com.quasar.dorm.query
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.DataObject
import com.quasar.dorm.model.ObjectDescriptor
import com.quasar.dorm.ObjectManager
import com.quasar.dorm.persistence.DataObjectMapper
import com.quasar.dorm.persistence.ObjectReader
import com.quasar.dorm.persistence.entity.AttributeEntity
import com.quasar.dorm.persistence.entity.EntityEntity
import com.quasar.dorm.transaction.TransactionState
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root
import jakarta.persistence.criteria.Subquery

class QueryManager(val objectManager: ObjectManager, private val entityManager: EntityManager, private val mapper: DataObjectMapper) {
    // instance data

    private val builder = entityManager.criteriaBuilder

    // Expressions

    fun from(objectDescriptor: ObjectDescriptor) : From {
        return From(objectDescriptor)
    }

    fun create() : Query<DataObject> {
        return Query(DataObject::class.java, this, objectManager)
    }

    fun <T : Any> create(result: Class<T>) : Query<T> {
        return Query(result, this, objectManager)
    }

    fun <T : Any> execute(query: Query<T>, executor: QueryExecutor<T>) : QueryResult<T> {
        return QueryResult(query, computeQueryResult<T>(query, executor))
    }

    // private

    private fun <T : Any> preferJSON(objectQuery: Query<T>) : Boolean {
        return objectQuery.projection == null
    }

    private fun <T : Any> computeQueryResult(objectQuery: Query<T>, executor: QueryExecutor<T>) : List<T> {
        // create main query

        val readJSON = preferJSON(objectQuery) // TODO

        // object query

        if (objectQuery.projection != null) {
            val criteriaQuery = builder.createQuery(AttributeEntity::class.java)
            val attributeEntity = criteriaQuery.from(AttributeEntity::class.java)

            criteriaQuery.select(attributeEntity)

            if ( objectQuery.where != null)
                criteriaQuery.where(
                    objectQuery.where!!.createWhere(executor as QueryExecutor<Any>, builder, criteriaQuery as CriteriaQuery<Any>, attributeEntity as Root<Any>),
                    builder.or(*objectQuery.projection!!.map { objectPath: ObjectPath -> builder.equal(attributeEntity.get<String>("attribute"), objectPath.attributeName()) }.toTypedArray())
                )

            else
                criteriaQuery.where(
                    builder.or(*objectQuery.projection!!.map { objectPath: ObjectPath -> builder.equal(attributeEntity.get<String>("attribute"), objectPath.attributeName()) }.toTypedArray())
                )

            criteriaQuery.orderBy(builder.asc(attributeEntity.get<Int>("entity")))

            return computeProjectionResultFromAttributes(objectQuery.projection!!, entityManager.createQuery(criteriaQuery).resultList as List<AttributeEntity>) as List<T>
        }
        else {
            val criteriaQuery = builder.createQuery(EntityEntity::class.java)
            val attributeEntity = criteriaQuery.from(EntityEntity::class.java)

            criteriaQuery.select(attributeEntity)

            if ( objectQuery.where !== null)
                criteriaQuery.where(
                    objectQuery.where!!.createWhere(executor as QueryExecutor<Any>, builder, criteriaQuery as CriteriaQuery<Any>, attributeEntity as Root<Any>),
                )

            //return computeObjectResultFromJSON(objectQuery.root!!.objectDescriptor, objectManager.transactionState(), criteriaQuery)

            val entities = entityManager.createQuery(criteriaQuery).resultList

            // compute result

            val state = objectManager.transactionState()
            val objectDescriptor = objectQuery.root!!.objectDescriptor

            return entities.map { entity -> mapper.readFromEntity(state, objectDescriptor, entity) } as List<T>

            //TODOcriteriaQuery.orderBy(builder.asc(attributeEntity.get<Int>("entity")))

            //TODOreturn computeObjectResultFromAttributes(objectQuery.root!!.objectDescriptor, objectManager.transactionState(), entityManager.createQuery(criteriaQuery).resultList  as List<AttributeEntity>) as List<T>
        }
    }

    private fun computeObjectResultFromAttributes(objectDescriptor: ObjectDescriptor, state: TransactionState, attributes: List<AttributeEntity>) : List<DataObject>  {
        // compute result

        val result = ArrayList<DataObject>()

        var entity = -1
        var start = -1
        var index = 0

        for (attribute in attributes) {
            if (attribute.entity != entity) {
                entity = attribute.entity

                if (start >= 0)
                    result.add(mapper.read(state, objectDescriptor, attributes, start, index - 1))

                start = index
            }

            index++
        } // for

        if (start in 0..<index)
            result.add(mapper.read(state, objectDescriptor, attributes, start, index - 1))

        // done

        return result
    }

    private fun computeProjectionResultFromAttributes(projection: Array<out ObjectPath>, attributes: List<AttributeEntity>) : List<Array<Any?>> {
        // setup mapping

        val name2Index = HashMap<String,Int>()

        var i = 0
        for ( pro in projection)
            name2Index[(pro as PropertyPath).property.name] = i++

        val reader = projection.map { p -> ObjectReader.valueReader( p.type()) }.toTypedArray()

        // local function

        fun create(start: Int, end: Int) : Array<Any?> {
            val tuple = arrayOfNulls<Any>(projection.size)

            for (ai in start..end) {
                val attr = attributes[ai]

                val resultIndex = name2Index[attr.attribute]!!

                tuple[resultIndex] = reader[resultIndex](attr)
            } // for

            return tuple
        }

        // go

        val result = ArrayList<Array<Any?>>()

        var entity = -1
        var start = -1
        var index = 0
        for (attribute in attributes) {
            if (attribute.entity != entity) {
                entity = attribute.entity

                if (start >= 0)
                    result.add( create(start, index - 1))

                start = index
            }

            index++
        } // for

        if (start in 0..<index)
            result.add(create(start, index - 1))

        // done

        return result
    }
}