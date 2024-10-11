package org.sirius.dorm.model
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
enum class Multiplicity(val optional: Boolean, val multiValued: Boolean) {
    ZERO_OR_ONE(true, false),
    ONE(false, false),
    MANY(false, true),
    ZERO_OR_MANY(true, true)
}