package org.sirius.dorm.persistence.entity
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import jakarta.persistence.*
import org.sirius.dorm.transaction.TransactionState
import java.time.LocalDateTime
import java.util.ArrayList

@Embeddable
data class EntityStatus(var created: LocalDateTime, var createdBy: String, var modified: LocalDateTime, var modifiedBy: String) {
    companion object {
        val NEW = EntityStatus( LocalDateTime.now(), "",  LocalDateTime.now(),"")

        fun from(status: EntityStatus) : EntityStatus {
            return EntityStatus(
                status.created,
                status.createdBy,
                status.modified,
                status.modifiedBy,
            )
        }
    }
}


@Entity
@Table(name="ENTITY")
data class EntityEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    var id : Long,

    @Column(name = "TYPE")
    var type : String,

    @Version
    @Column(name = "VERSION_COUNTER")
    var versionCounter : Long,

    @Embedded
    var status: EntityStatus? = null,

    @OneToMany(mappedBy = "entity", cascade = [CascadeType.ALL], orphanRemoval = true)
    var properties : MutableList<PropertyEntity> = ArrayList()
) {
    // listeners

    @PrePersist
    fun prePersist() {
        TransactionState.current().onPrePersist(this)
    }

    @PreUpdate
    fun preUpdate() {
        TransactionState.current().onPreUpdate(this)
    }

    // override Object

    override fun toString(): String {
        return "${type}[${id}]"
    }

    override fun equals(other: Any?): Boolean {
        if ( other is EntityEntity) {
            return this.id == other.id
        }
        else
            return false
    }

    override fun hashCode(): Int {
        return id.hashCode() + type.hashCode()
    }
}