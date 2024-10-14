package org.sirius.dorm.transaction

/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

enum class Status(val intValue: Int) {
    MANAGED(1 shl 0),
    CREATED(1 shl 1),
    DELETED(1 shl 2)
}