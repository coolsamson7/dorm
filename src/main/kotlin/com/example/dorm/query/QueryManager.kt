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
import jakarta.persistence.criteria.Subquery

class QueryManager(public val objectManager: ObjectManager, private val entityManager: EntityManager, private val mapper: DataObjectMapper) {
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

        val readJSON = preferJSON(objectQuery)

        val criteriaQuery   = builder.createQuery(AttributeEntity::class.java)
        val criteriaEntityQuery = builder.createQuery(EntityEntity::class.java)

        // where

        var subQuery = if ( readJSON ) criteriaEntityQuery.subquery(Int::class.java) else criteriaQuery.subquery(Int::class.java)
        val attribute = subQuery.from(AttributeEntity::class.java) // type: string, entity: Int

        if (objectQuery.where !== null) {
            subQuery
                .select(attribute.get("entity"))
                .distinct(true)
                .where(
                    // object type

                    builder.equal(attribute.get<String>("type"), objectQuery.root!!.objectDescriptor.name),

                    // the attribute

                    builder.equal(attribute.get<String>("attribute"), (objectQuery.where!!.path as PropertyPath).property.name), // TODO

                    // the actual expression

                    objectQuery.where!!.createWhere(executor as QueryExecutor<Any>, builder, attribute)
                )
        }
        else subQuery = subQuery
            .select(attribute.get("entity"))
            .distinct(true)
            .where(builder.equal(attribute.get<String>("type"), objectQuery.root!!.objectDescriptor.name))

        // either object or projection

        return if (objectQuery.projection == null) {
            if ( true)
                computeObjectResultFromJSON(
                    objectQuery.root!!.objectDescriptor,
                    objectManager.transactionState(),
                    criteriaEntityQuery,
                    subQuery
                ) as List<T>
            else
                computeObjectResult(
                    objectQuery.root!!.objectDescriptor,
                    objectManager.transactionState(),
                    criteriaQuery,
                    subQuery
                ) as List<T>
        }
        else
            computeProjectionResult(objectQuery.projection!!, criteriaQuery, subQuery) as List<T>
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

    private fun computeObjectResult(objectDescriptor: ObjectDescriptor, state: TransactionState, criteriaQuery: CriteriaQuery<AttributeEntity>, subQuery: Subquery<Int>) : List<DataObject> {
        val attributeEntity = criteriaQuery.from(AttributeEntity::class.java)

        // object query

        criteriaQuery
            .select(attributeEntity)
            .where(builder.`in`(attributeEntity.get<Int>("entity")).value(subQuery))
            .orderBy(builder.asc(attributeEntity.get<Int>("entity")))//, attributeEntity.get<Int>("attribute"))

        // execute

        val attributes = entityManager.createQuery(criteriaQuery).resultList

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

        if (index > start && start >= 0)
            result.add(mapper.read(state, objectDescriptor, attributes, start, index - 1))

        // done

        return result
    }

    private fun computeProjectionResult(projection: Array<out ObjectPath>, criteriaQuery: CriteriaQuery<AttributeEntity>, subQuery: Subquery<Int>) : List<Array<Any?>> {
        val attributeEntity = criteriaQuery.from(AttributeEntity::class.java)

        // where

        criteriaQuery
            .select(attributeEntity)
            .where(
                builder.`in`(attributeEntity.get<Int>("entity")).value(subQuery),
                *projection.map { objectPath: ObjectPath -> builder.equal(attributeEntity.get<String>("attribute"), (objectPath as PropertyPath).property.name) }.toTypedArray()
            )
            .orderBy(builder.asc(attributeEntity.get<Int>("entity")))
            //.groupBy(attributeEntity.get<Int>("entity"), attributeEntity.get<Int>("attribute"))

        // execute

        val attributes = entityManager.createQuery(criteriaQuery).resultList

        // setup mapping

        val name2Index = HashMap<String,Int>()

        var i = 0
        for ( pro in projection)
            name2Index[(pro as PropertyPath).property.name] = i++

        val reader = projection.map { p -> ObjectReader.valueReader( (p as PropertyPath).property.type.baseType) }.toTypedArray()

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

        if (index > start && start >= 0)
            result.add(create(start, index - 1))

        // done

        return result
    }
}