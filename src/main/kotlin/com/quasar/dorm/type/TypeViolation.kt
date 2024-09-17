package com.quasar.dorm.type
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
class TypeViolation(
    /**
     * the type name
     */
    type: Class<Any>,
    /**
     * the constraint name
     */
    name: String,
    /**
     * any parameters of the constraint
     */
    //params: any
    /**
     * the value
     */
    value: Any,
    /**
     * the path
     */
    path: String,
    /**
     * optional message
     */
    //message?: String
)