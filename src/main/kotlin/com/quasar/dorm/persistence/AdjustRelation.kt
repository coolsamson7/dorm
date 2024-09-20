package com.quasar.dorm.persistence
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.dorm.`object`.Relation
import com.quasar.dorm.transaction.Operation

class AdjustRelation(val relation: Relation) : Operation() {
    override fun execute() {
        relation.flush()
    }
}