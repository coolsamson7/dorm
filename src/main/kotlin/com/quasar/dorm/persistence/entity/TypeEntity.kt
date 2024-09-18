package com.quasar.dorm.persistence.entity
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import jakarta.persistence.*


@Entity
@Table(name="TYPE")
class TypeEntity(
    @Id
    var name : String,

    @Column(name = "JSON", length = 1024)
    var json : String
)