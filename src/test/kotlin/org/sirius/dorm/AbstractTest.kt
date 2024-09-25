package org.sirius.dorm
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.hibernate.Session
import org.hibernate.stat.Statistics
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.sirius.common.tracer.TraceLevel
import org.sirius.common.tracer.Tracer
import org.sirius.common.tracer.trace.ConsoleTrace
import org.sirius.common.type.base.*
import org.sirius.dorm.model.Multiplicity
import org.sirius.dorm.model.ObjectDescriptor
import org.sirius.dorm.`object`.DataObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component


@Configuration()
@Import(DORMConfiguration::class)
class TestConfiguration {
}

data class ColumnType(val col: String, val type: Class<*>, val format: String)

@Component
class TablePrinter {
    // instance

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    val entity : Array<ColumnType> = arrayOf(
        ColumnType("ID  ", Long::class.java, "%4d"),
        ColumnType("TYPE      ", String::class.java, "%-10s")
    )

    val relations : Array<ColumnType> = arrayOf(
        ColumnType("FROM_ENTITY", String::class.java, "%-11s"),
        ColumnType("FROM_", Long::class.java, "%5d"),
        ColumnType("TO_ENTITY", String::class.java, "%-11s"),
        ColumnType("TO_", Long::class.java, "%5d")
    )

    val property : Array<ColumnType> = arrayOf(
        ColumnType("ENTITY    ", Long::class.java,"%-10d"),
        ColumnType("ATTRIBUTE ", String::class.java, "%-10s"),
        ColumnType("TYPE      ", String::class.java, "%-10s"),
        ColumnType("STRING_VALUE", String::class.java, "%-11s"),
        ColumnType("INT_VALUE ", Int::class.java, "%-10d"),
        ColumnType("DOUBLE_VALUE", Double::class.java, "%-12f")
    )

    // public

    fun printAll() {
        print("ENTITY", entity)
        print("PROPERTY", property)
        print("RELATIONS", relations)
    }

    fun print(entity: String, columns: Array<ColumnType> ) {
        println(entity)
        for ( col in columns)
            print(col.col + " | ")
        println()

        jdbcTemplate.query<List<String>>("SELECT * FROM ${entity} ") { rs, _ ->
            columns.map { column -> when (column.type) {
                Long::class.java -> String.format(column.format, rs.getLong(column.col.strip()))
                Int::class.java -> String.format(column.format, rs.getInt(column.col.strip()))
                Double::class.java -> String.format(column.format, rs.getDouble(column.col.strip()))
                String::class.java -> String.format(column.format, rs.getString(column.col.strip()))
                else -> {
                    "";
                }
            }
            }
        }.forEach { row  ->
            for ( col in row ) {
                print(col + " | ")
            }
            println()
        }

    }
}

@SpringBootTest(classes =[TestConfiguration::class])
class AbstractTest {
    @Autowired
    lateinit var objectManager : ObjectManager
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    protected var personDescriptor : ObjectDescriptor? = null

    @Autowired
    lateinit var printer: TablePrinter

    init {
        Tracer(ConsoleTrace(), "%t{yyyy-MM-dd HH:mm:ss,SSS} %l{-10s} [%p] %m")
            .setTraceLevel("", TraceLevel.OFF)
            .setTraceLevel("com", TraceLevel.LOW)
            .setTraceLevel("com.sirius.dorm", TraceLevel.HIGH)
    }

    lateinit var statistics : Statistics;

    protected fun printTables() {
      printer.printAll()
    }

    //@BeforeEach
    fun clearStats() {
        val session = entityManager.unwrap(Session::class.java)

        statistics = session.getSessionFactory().getStatistics()
    }

    //@AfterEach
    fun printStats() {
        for ( query in statistics.queries)
            println(query)
    }

    @BeforeEach
    fun setupSchema() {
        withTransaction {
            if ( objectManager.findDescriptor("person") == null) {
                val stringType = string().length(100)
                val intType = int().greaterEqual(0)

                objectManager.type("person")
                    .attribute("name", stringType)
                    .attribute("age", intType)

                    // relations

                    .relation("father", "person", Multiplicity.ZERO_OR_ONE, "children")
                    .relation("children", "person", Multiplicity.ZERO_OR_MANY)

                    .attribute("boolean", boolean())
                    .attribute("string", string())
                    .attribute("short", short())
                    .attribute("int", int())
                    .attribute("long", long())
                    .attribute("float", float())
                    .attribute("double", double())
                    .register()
            }

            personDescriptor = objectManager.getDescriptor("person")
        }
    }

    // delete

    @AfterEach
    fun deletePerson() {
        val queryManager = objectManager.queryManager()
        objectManager.begin()
        try {
            val person = queryManager.from(personDescriptor!!)

            // no where

            val query = queryManager.create().from(person) // object query
            val queryResult = query.execute().getResultList()

            for (result in queryResult)
                objectManager.delete(result)
        }
        finally {
            objectManager.commit()
        }
    }

    protected fun withTransaction(doIt: () -> Unit) {
        objectManager.begin()
        try {
            doIt()
        }
        finally {
            objectManager.commit()
        }
    }

    protected fun readPersons(): List<DataObject> {
        val queryManager = objectManager.queryManager()
        val person = queryManager.from(personDescriptor!!)

        // no where

        val query = queryManager
            .create()
            .select(person)
            .from(person)

        return query.execute().getResultList()
    }

    protected fun createPerson(name: String, age: Int) : Long {
        objectManager.begin()
        try {
            val person = objectManager.create(personDescriptor!!)

            person["name"] = name
            person["age"] = age

            return person.id
        }
        finally {
            objectManager.commit()
        }
    }
}