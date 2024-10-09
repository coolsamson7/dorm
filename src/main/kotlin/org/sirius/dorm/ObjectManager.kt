package org.sirius.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import org.sirius.dorm.model.*
import org.sirius.dorm.`object`.DataObject
import org.sirius.dorm.persistence.DataObjectMapper
import org.sirius.dorm.persistence.entity.EntityEntity
import org.sirius.dorm.query.*
import org.sirius.dorm.transaction.Status
import org.sirius.dorm.transaction.TransactionState
import jakarta.persistence.EntityManager
import jakarta.persistence.LockModeType
import jakarta.persistence.PersistenceContext
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RecognitionException
import org.sirius.dorm.query.parser.OQLParser
import org.sirius.dorm.session.SessionContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import java.util.concurrent.ConcurrentHashMap

@Component
class ObjectManager() {
    // instance data

    val descriptors = ConcurrentHashMap<String, ObjectDescriptor>()

    @PersistenceContext
    lateinit var entityManager: EntityManager
    @Autowired
    private lateinit var transactionManager: PlatformTransactionManager
    @Autowired
    lateinit var mapper: DataObjectMapper
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    lateinit var sessionContext: SessionContext

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

        descriptors[objectDescriptor.name] = objectDescriptor

        return this
    }

    fun findDescriptor(name: String) : ObjectDescriptor? {
        var descriptor = descriptors[name]

        if ( descriptor == null) {
            descriptor = objectDescriptorStorage.findByName(name)
            if ( descriptor != null)
                descriptors[name] = descriptor
        }

        descriptor?.resolve(this)

        return descriptor
    }

    fun descriptors() :Collection<ObjectDescriptor> {
        return descriptors.values
    }

    fun getDescriptor(name: String) : ObjectDescriptor {
        val descriptor = findDescriptor(name)

        if ( descriptor != null)
            return descriptor
        else
            throw ObjectManagerError("unknown type ${name}")
    }

    fun create(objectDescriptor: ObjectDescriptor) : DataObject {
        return TransactionState.current().create(objectDescriptor.create(Status.CREATED))
    }

    fun delete(obj: DataObject) {
        obj.delete()
    }

    // tx

    fun begin() {
        TransactionState.set(this, transactionManager)
    }

    fun commit() {
        try {
            TransactionState.current().commit(mapper)
        }
        finally {
            mapper.clear()
            TransactionState.remove()
        }
    }

    fun rollback() {
        try {
            TransactionState.current().rollback(mapper)
        }
        finally {
            mapper.clear()
            TransactionState.remove()
        }
    }

    fun queryManager() : QueryManager {
        return QueryManager(this, entityManager, mapper)
    }

    fun <T: Any> query(query: String, resultType: Class<T> = DataObject::class.java as Class<T>) : Query<T> {
        val tokenStream = CommonTokenStream(org.sirius.dorm.query.parser.OQLLexer(CharStreams.fromString(query)))

        val parser = OQLParser(tokenStream)

        try {
            parser.file()

            return parser.select!!.transform(queryManager())
        }
        catch (e: RecognitionException) {
            throw IllegalArgumentException(e.message)
        }
    }

    fun findById(descriptor: ObjectDescriptor, id: Long) : DataObject? {
        val entity = entityManager.find(EntityEntity::class.java, id, LockModeType.OPTIMISTIC)

        return if ( entity !== null)
            mapper.read(TransactionState.current(), descriptor, entity)
        else
            null
    }

    // companion

    companion object {
        lateinit var instance : ObjectManager
    }
}