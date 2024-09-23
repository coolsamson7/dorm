package org.sirius.dorm.persistence.entity
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import jakarta.persistence.*

@Entity
@Table(name="ENTITY_SCHEMA")
class EntitySchemaEntity (
    @Column(name = "TYPE")
    @Id
    var type : String,

    @Column(name = "JSON", length = 1024)
    var json : String
 )