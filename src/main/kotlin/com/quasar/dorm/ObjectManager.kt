package com.quasar.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import com.quasar.dorm.model.ObjectDescriptor
import com.quasar.dorm.model.ObjectDescriptorStorage
import com.quasar.dorm.model.PropertyDescriptor
import com.quasar.dorm.model.persistence.PersistentObjectDescriptorStorage
import com.quasar.dorm.persistence.DataObjectMapper
import com.quasar.dorm.query.*
import com.quasar.dorm.query.parser.OQLLexer
import com.quasar.dorm.query.parser.OQLParser
import com.quasar.dorm.transaction.ObjectState
import com.quasar.dorm.transaction.Status
import com.quasar.dorm.transaction.TransactionState
import com.quasar.dorm.type.Type
import com.quasar.dorm.type.base.int
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RecognitionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import java.util.concurrent.ConcurrentHashMap


class ObjectDescriptorBuilder(val manager: ObjectManager, val name: String) {
    // instance data

    private val attributes = ArrayList<PropertyDescriptor<Any>>()

    init {
        attribute("id", int(), true)
    }

    // fluent

    fun <T: Any> attribute(name: String, type: Type<T>, isPrimaryKey: Boolean = false) : ObjectDescriptorBuilder {
        attributes.add(PropertyDescriptor(name, type as Type<Any>, isPrimaryKey))

        return this
    }

    // public

    fun register() : ObjectDescriptor {
        val descriptor = ObjectDescriptor(name, attributes.toTypedArray())

        manager.register(descriptor)

        return descriptor
    }
}

@Component
class ObjectManager() {
    // instance data

    val descriptors = ConcurrentHashMap<String, ObjectDescriptor>()

    @PersistenceContext
    private lateinit var entityManager: EntityManager
    @Autowired
    private lateinit var transactionManager: PlatformTransactionManager
    @Autowired
    private lateinit var mapper: DataObjectMapper

    @Autowired
    private lateinit var objectDescriptorStorage: ObjectDescriptorStorage

    init {
        instance = this
    }

    // register

    fun type(name: String) : ObjectDescriptorBuilder {
        return ObjectDescriptorBuilder(this, name)
    }

    fun register(objectDescriptor: ObjectDescriptor) : ObjectManager {
        if ( descriptors.containsKey(objectDescriptor.name))
            throw ObjectManagerError("already registered type ${objectDescriptor.name}")

        objectDescriptorStorage.store(objectDescriptor)

        descriptors.put(objectDescriptor.name, objectDescriptor)

        return this
    }

    fun find(name: String) : ObjectDescriptor? {
        var descriptor = descriptors.get(name)

        if ( descriptor == null) {
            descriptor = objectDescriptorStorage.findByName(name)
            if ( descriptor != null) {
                descriptors.put(name, descriptor)
            }
        }

        return descriptor
    }

    fun get(name: String) : ObjectDescriptor {
        val descriptor = find(name)

        if ( descriptor != null)
            return descriptor
        else
            throw ObjectManagerError("unknown type ${name}")
    }

    fun create(objectDescriptor: ObjectDescriptor) : DataObject {
        val obj = objectDescriptor.create()

        transactionState().register(ObjectState(obj, Status.CREATED))

        return obj
    }

    fun delete(obj: DataObject) {
        if ( obj.state != null)
            obj.state!!.status = Status.DELETED
    }

    // tx

    fun begin() {
        transactionState.set(TransactionState(this, transactionManager))
    }

    fun commit() {
        try {
            commitTransaction(transactionState())
        }
        finally {
            mapper.clear()
            transactionState.remove()
        }
    }

    fun rollback() {
        try {
            rollbackTransaction(transactionState())
        }
        finally {
            mapper.clear()
            transactionState.remove()
        }
    }

    fun transactionState() : TransactionState {
        return transactionState.get()
    }

    fun queryManager() : QueryManager {
        return QueryManager(this, entityManager, mapper)
    }

    fun <T: Any> query(query: String, resultType: Class<T> = DataObject::class.java as Class<T>) : Query<T> {
        val tokenStream = CommonTokenStream(OQLLexer(CharStreams.fromString(query)))

        val parser = OQLParser(tokenStream)

        parser.setup(queryManager())
        try {
            parser.file()

            return parser.select!!.transform<T>(parser.queryManager)
        }
        catch (e: RecognitionException) {
            throw IllegalArgumentException(e.message)
        }
    }

    fun findById(descriptor: ObjectDescriptor, id: Int) : DataObject? {
        val queryManager=  queryManager()
        val obj = queryManager.from(descriptor)
        val query =  queryManager
            .create()
            .select(obj)
            .where(eq(obj.get("id"), id))

        return query.execute().getSingleResult()
    }

    // private

    private fun rollbackTransaction(transactionState: TransactionState) {
        for (state in transactionState.states) {
            when ( state.status) {
                Status.MANAGED -> if ( state.isDirty()) state.rollback()

                else  -> {} ; // noop
            }
        }

        transactionState.rollback()
    }

    private fun commitTransaction(transactionState: TransactionState) {
        // new objects

        for (state in transactionState.states) {
            when ( state.status) {
                Status.CREATED ->  mapper.create(state.obj)
                Status.DELETED -> mapper.delete(state.obj)
                Status.MANAGED -> {
                    if ( state.isDirty())
                        mapper.update(state.obj)
                }
            }
        }

        transactionState.commit()
    }

    // companion

    companion object {
        val transactionState = ThreadLocal<TransactionState>()

        lateinit var instance : ObjectManager
    }
}