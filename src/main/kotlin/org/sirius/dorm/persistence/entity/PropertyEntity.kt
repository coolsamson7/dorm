package org.sirius.dorm.persistence.entity
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import jakarta.persistence.*
import org.sirius.dorm.`object`.DataObject
import java.io.Serializable

data class PropertyId(private val entity: Int = 0, private val attribute: String = "") : Serializable

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

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    @JoinTable(
        name = "RELATION",
        joinColumns = [JoinColumn(name = "FROM_ENTITY"), JoinColumn(name = "FROM")],
        inverseJoinColumns = [JoinColumn(name = "TO_ENTITY"), JoinColumn(name = "TO")]
    )
    val targets : MutableSet<PropertyEntity> = HashSet(),

    @ManyToMany(mappedBy="targets", fetch = FetchType.LAZY)
    val sources : MutableSet<PropertyEntity> = HashSet(),
) {
    // override Object

    /*override fun equals(other: Any?): Boolean { TODO
        if ( other is DataObject)
            return id == other.id && this.type === other.type
        else
            return false
    }*/

    override fun hashCode(): Int {
        return type.hashCode() + attribute.hashCode()
    }
}