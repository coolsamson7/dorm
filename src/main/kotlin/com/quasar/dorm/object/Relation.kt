package com.quasar.dorm.`object`

import com.quasar.dorm.persistence.entity.AttributeEntity

abstract class Relation(var property: AttributeEntity?) : Property() {
}