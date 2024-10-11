package org.sirius.dorm.persistence.entity
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import jakarta.persistence.Embeddable
import java.time.LocalDateTime

@Embeddable
data class EntityStatus(var created: LocalDateTime, var createdBy: String, var modified: LocalDateTime, var modifiedBy: String) {
    companion object {
        val NEW = EntityStatus(LocalDateTime.now(), "", LocalDateTime.now(), "")

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