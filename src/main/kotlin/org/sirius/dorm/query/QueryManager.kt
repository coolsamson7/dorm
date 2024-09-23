package org.sirius.dorm.query
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.`object`.DataObject
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.ObjectManager
import org.sirius.dorm.persistence.DataObjectMapper
import org.sirius.dorm.persistence.ObjectReader
import org.sirius.dorm.persistence.entity.PropertyEntity
import org.sirius.dorm.transaction.TransactionState
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root

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
        // flush managed objects that could influence query results
        // currently this is only the root, since we don't support joins yet

        TransactionState.current().flush(objectQuery.root!!.objectDescriptor)

        // create main query

        if (objectQuery.projection != null) {
            val criteriaQuery = builder.createQuery(PropertyEntity::class.java)
            val propertyEntity = criteriaQuery.from(PropertyEntity::class.java)

            criteriaQuery.select(propertyEntity)

            //TODO builder.equal(attributeEntity.get<String>("type"), objectQuery.root!!.objectDescriptor.name),
            if ( objectQuery.where != null)
                criteriaQuery.where(
                    objectQuery.where!!.createWhere(executor as QueryExecutor<Any>, builder, criteriaQuery as CriteriaQuery<Any>, propertyEntity as Root<Any>),
                    builder.or(*objectQuery.projection!!.map { objectPath: ObjectPath -> builder.equal(propertyEntity.get<String>("attribute"), objectPath.attributeName()) }.toTypedArray())
                )

            else
                criteriaQuery.where(
                    builder.or(*objectQuery.projection!!.map { objectPath: ObjectPath -> builder.equal(propertyEntity.get<String>("attribute"), objectPath.attributeName()) }.toTypedArray())
                )

            criteriaQuery.orderBy(builder.asc(propertyEntity.get<Int>("entity")))

            return computeProjectionResultFromAttributes(objectQuery.projection!!, entityManager.createQuery(criteriaQuery).resultList as List<PropertyEntity>) as List<T>
        }
        else {
            val criteriaQuery = builder.createQuery(PropertyEntity::class.java)
            val propertyEntity = criteriaQuery.from(PropertyEntity::class.java)

            criteriaQuery.select(propertyEntity)

            if ( objectQuery.where !== null)
                criteriaQuery.where(
                    builder.equal(propertyEntity.get<String>("type"), objectQuery.root!!.objectDescriptor.name),
                    objectQuery.where!!.createWhere(executor as QueryExecutor<Any>, builder, criteriaQuery as CriteriaQuery<Any>, propertyEntity as Root<Any>),
                )
            else  criteriaQuery.where(
                builder.equal(propertyEntity.get<String>("type"), objectQuery.root!!.objectDescriptor.name),
            )

            criteriaQuery.orderBy(builder.asc(propertyEntity.get<Int>("entity")))

            /*
            val entities = entityManager.createQuery(criteriaQuery).resultList

            // compute result

            val state = TransactionState.transactionState()
            val objectDescriptor = objectQuery.root!!.objectDescriptor

            return entities.map { entity -> mapper.readFromEntity(state, objectDescriptor, entity) } as List<T>
            */

            return computeObjectResultFromAttributes(objectQuery.root!!.objectDescriptor, TransactionState.current(), entityManager.createQuery(criteriaQuery).resultList  as List<PropertyEntity>) as List<T>
        }
    }

    private fun computeObjectResultFromAttributes(objectDescriptor: ObjectDescriptor, state: TransactionState, attributes: List<PropertyEntity>) : List<DataObject>  {
        // compute result

        val result = ArrayList<DataObject>()

        var entity = -1
        var start = -1
        var index = 0

        for (attribute in attributes) {
            if (attribute.entity.id != entity) {
                entity = attribute.entity.id

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

    private fun computeProjectionResultFromAttributes(projection: Array<out ObjectPath>, attributes: List<PropertyEntity>) : List<Array<Any?>> {
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
            if (attribute.entity.id != entity) {
                entity = attribute.entity.id

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