package com.quasar.dorm.model

/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

interface ObjectDescriptorStorage {
    fun store(objectDescriptor: ObjectDescriptor)

    fun findByName(name: String) : ObjectDescriptor?
}