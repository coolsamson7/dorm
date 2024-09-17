package com.example.dorm.query

import com.example.dorm.DataObject
import com.example.dorm.model.ObjectDescriptor
import com.example.dorm.ObjectManager
import com.example.dorm.persistence.DataObjectMapper
import com.example.dorm.persistence.ObjectReader
import com.example.dorm.persistence.entity.AttributeEntity
import com.example.dorm.persistence.entity.EntityEntity
import com.example.dorm.transaction.TransactionState
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

        val criteriaQuery : CriteriaQuery<AttributeEntity> = builder.createQuery(AttributeEntity::class.java)
        val attributeEntity = criteriaQuery.from(AttributeEntity::class.java)

        criteriaQuery.select(attributeEntity)

        if (objectQuery.projection != null) {
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
            if ( objectQuery.where != null)
                criteriaQuery.where(
                    objectQuery.where!!.createWhere(executor as QueryExecutor<Any>, builder, criteriaQuery as CriteriaQuery<Any>, attributeEntity as Root<Any>),
                )

            criteriaQuery.orderBy(builder.asc(attributeEntity.get<Int>("entity")))

            return computeObjectResultFromAttributes(objectQuery.root!!.objectDescriptor, objectManager.transactionState(), entityManager.createQuery(criteriaQuery).resultList  as List<AttributeEntity>) as List<T>
        }
    }

    private fun computeObjectResultFromJSON(objectDescriptor: ObjectDescriptor, state: TransactionState, criteriaQuery: CriteriaQuery<EntityEntity>, subQuery: Subquery<Int>) : List<DataObject> {
        val entityEntity = criteriaQuery.from(EntityEntity::class.java)

        // object query

        criteriaQuery
            .select(entityEntity)
            .where(builder.`in`(entityEntity.get<Int>("id")).value(subQuery))

        // execute

        val entities = entityManager.createQuery(criteriaQuery).resultList

        // compute result

        return entities.map { entity -> mapper.readFromEntity(state, objectDescriptor, entity) }
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