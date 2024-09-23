package org.sirius.dorm.persistence.entity
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import jakarta.persistence.*
import java.util.ArrayList

@Entity
@Table(name="ENTITY")
data class EntityEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    var id : Int,

    @Column(name = "TYPE")
    var type : String,

    @Column(name = "JSON")
    var json : String,

    @OneToMany(mappedBy = "entity", cascade = [CascadeType.ALL], orphanRemoval = true)
    var properties : MutableList<PropertyEntity> = ArrayList()
)