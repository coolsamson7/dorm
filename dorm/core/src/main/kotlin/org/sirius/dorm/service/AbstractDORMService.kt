package org.sirius.dorm.service

import org.sirius.dorm.ObjectManager
import org.springframework.beans.factory.annotation.Autowired
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
abstract class AbstractDORMService {
    // instance data

    @Autowired
    lateinit var objectManager: ObjectManager

    // protected

    protected fun <T> withTransaction(doIt: () -> T) : T {
        objectManager.begin()
        var committed = false
        try {
            val result = doIt()

            committed = true
            objectManager.commit()

            return result
        }
        catch (throwable: Throwable) {
            if ( !committed )
                objectManager.rollback()

            throw throwable
        }
    }
}