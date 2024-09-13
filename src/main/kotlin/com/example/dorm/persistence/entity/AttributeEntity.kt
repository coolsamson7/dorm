package com.example.dorm.persistence.entity

import jakarta.persistence.*
import java.io.Serializable

data class AttributeId(private val entity: Int = 0, private val attribute: String = "") : Serializable

@Entity
@Table(name="ATTRIBUTE")
@IdClass(AttributeId::class)
data class AttributeEntity(
    @Column(name = "ENTITY")
    @Id
    var entity : Int,

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
    var doubleValue : Double
)