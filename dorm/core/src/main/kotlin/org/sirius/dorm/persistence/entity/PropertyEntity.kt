package org.sirius.dorm.persistence.entity
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import jakarta.persistence.*
import java.io.Serializable

data class PropertyId(private val entity: Long = 0L, private val attribute: String = "") : Serializable

@Entity
@Table(name="PROPERTY",
    indexes = [
        Index(columnList = "TYPE"),
        Index(columnList = "INT_VALUE"),
        Index(columnList = "DOUBLE_VALUE"),
        Index(columnList = "STRING_VALUE")
    ]
)
@IdClass(PropertyId::class)
data class PropertyEntity(
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH])
    @JoinTable(
        name = "RELATIONS",
        joinColumns = [JoinColumn(name = "FROM_ATTR"), JoinColumn(name = "FROM_ENTITY")],
        inverseJoinColumns = [JoinColumn(name = "TO_ATTR"), JoinColumn(name = "TO_ENTITY")]
    )
    val targets : MutableSet<PropertyEntity> = HashSet(),

    @ManyToMany(mappedBy="targets")//, cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH])
    val sources : MutableSet<PropertyEntity> = HashSet(),
) {
    // override Object

    override fun toString(): String {
        return "${type}.${attribute}[id]"
    }

    override fun equals(other: Any?): Boolean {
        return if ( other is PropertyEntity)
            this === other
        else
            false
    }

    override fun hashCode(): Int {
        return /*id.hashCode() +*/ type.hashCode() + attribute.hashCode()
    }
}