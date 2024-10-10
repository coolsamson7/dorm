package org.sirius.dorm.persistence
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.`object`.Relation
import org.sirius.dorm.transaction.Operation

class AdjustRelation(val relation: Relation) : Operation() {
    override fun execute() {
        relation.flush()
    }
}