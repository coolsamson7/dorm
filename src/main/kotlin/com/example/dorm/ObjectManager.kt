package com.example.dorm

import com.example.dorm.model.ObjectDescriptor
import com.example.dorm.model.PropertyDescriptor
import com.example.dorm.persistence.DataObjectMapper
import com.example.dorm.query.Query
import com.example.dorm.query.QueryManager
import com.example.dorm.query.parser.OQLLexer
import com.example.dorm.query.parser.OQLParser
import com.example.dorm.transaction.ObjectState
import com.example.dorm.transaction.Status
import com.example.dorm.transaction.TransactionState
import com.example.dorm.type.Type
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RecognitionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager


class ObjectDescriptorBuilder(val manager: ObjectManager, val name: String) {
    // instance data

    private val attributes = ArrayList<PropertyDescriptor<Any>>()

    // fluent

    fun <T: Any> attribute(name: String, type: Type<T>) : ObjectDescriptorBuilder {
        attributes.add(PropertyDescriptor(name, type as Type<Any>))

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

    val descriptors = HashMap<String, ObjectDescriptor>()

    @PersistenceContext
    private lateinit var entityManager: EntityManager
    @Autowired
    private lateinit var transactionManager: PlatformTransactionManager
    @Autowired
    private lateinit var mapper: DataObjectMapper

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

        descriptors.put(objectDescriptor.name, objectDescriptor)

        return this
    }

    fun get(name: String) : ObjectDescriptor {
        val descriptor = descriptors.get(name)
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
            transactionState.remove()
        }
    }

    fun rollback() {
        try {
            rollbackTransaction(transactionState())
        }
        finally {
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
        val tokenStream = CommonTokenStream(OQLLexer(ANTLRInputStream(query)))

        val parser = OQLParser(tokenStream)

        parser.setup(queryManager())
        try {
            parser.file()

            return parser.query as Query<T>!!
        }
        catch (e: RecognitionException) {
            throw IllegalArgumentException(e.message)
        }
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