package com.example.dorm.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name="ENTITY")
data class EntityEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id : Int,

    @Column(name = "TYPE")
    var type : String,

    @Column(name = "JSON")
    var json : String
)