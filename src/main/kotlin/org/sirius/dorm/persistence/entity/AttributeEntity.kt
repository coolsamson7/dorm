package org.sirius.dorm.persistence.entity
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import jakarta.persistence.*
import java.io.Serializable

data class AttributeId(private val entity: Int = 0, private val attribute: String = "") : Serializable

@Entity
@Table(name="ATTRIBUTE",
    indexes = [
        Index(columnList = "TYPE"),
        Index(columnList = "INT_VALUE"),
        Index(columnList = "DOUBLE_VALUE"),
        Index(columnList = "STRING_VALUE")
    ]
)
@IdClass(AttributeId::class)
data class AttributeEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "ENTITY")
    var entity : EntityEntity,

    @Column(name = "ATTRIBUTE")
    @Id
    var attribute : String,

    @Column(name = "TYPE")
    var type : String,

    @Column(name = "STRING_VALUE")
    var stringValue : String,

    @Column(name = "INT_VALUE")
    var intValue : Int,

    @Column(name = "DOUBLE_VALUE")
    var doubleValue : Double,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ATTRIBUTE_ENTITY",
        joinColumns = [JoinColumn(name = "ENTITY"), JoinColumn(name = "ATTRIBUTE")],
        inverseJoinColumns = [JoinColumn(name = "ID")]
    )
    val relations : MutableSet<EntityEntity> = HashSet()
)