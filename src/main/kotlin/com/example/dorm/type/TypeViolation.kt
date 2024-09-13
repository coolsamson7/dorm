package com.example.dorm.type

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