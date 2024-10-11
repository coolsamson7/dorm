package org.sirius.dorm.persistence.entity
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import jakarta.persistence.*
import org.sirius.dorm.transaction.TransactionState

@Entity
@Table(name="ENTITY_SCHEMA")
class EntitySchemaEntity (
    @Column(name = "TYPE")
    @Id
    var type : String,

    @Version
    @Column(name = "VERSION_COUNTER")
    var versionCounter : Long,

    @Embedded
    var status: EntityStatus? = null,

    @Column(name = "JSON", length = 1024)
    var json : String
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
}