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
import org.sirius.common.type.Type
import org.sirius.common.type.base.int
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RecognitionException
import org.sirius.dorm.query.parser.OQLParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import java.util.concurrent.ConcurrentHashMap


class ObjectDescriptorBuilder(val manager: ObjectManager, val name: String) {
    // instance data

    private val properties = ArrayList<PropertyDescriptor<Any>>()

    init {
        attribute("id", int(), true)
    }

    // fluent

    fun <T: Any> attribute(name: String, type: Type<T>, isPrimaryKey: Boolean = false) : ObjectDescriptorBuilder {
        properties.add(AttributeDescriptor(name, type as Type<Any>, isPrimaryKey))

        return this
    }

    fun relation(name: String, target: String, multiplicity: Multiplicity, cascade: Cascade? = null) : ObjectDescriptorBuilder {
        properties.add(RelationDescriptor(name, target, multiplicity, cascade, null))

        return this
    }

    fun relation(name: String, target: String, multiplicity: Multiplicity, inverse: String, cascade: Cascade? = null) : ObjectDescriptorBuilder {
        properties.add(RelationDescriptor(name, target, multiplicity, cascade, inverse))

        return this
    }

    // public

    fun register() {
        manager.register( ObjectDescriptor(name, properties.toTypedArray()))
    }
}

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

        parser.setup(queryManager())
        try {
            parser.file()

            return parser.select!!.transform<T>(parser.queryManager)
        }
        catch (e: RecognitionException) {
            throw IllegalArgumentException(e.message)
        }
    }

    fun findById(descriptor: ObjectDescriptor, id: Long) : DataObject? {
        val entity = entityManager.find(EntityEntity::class.java, id)

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