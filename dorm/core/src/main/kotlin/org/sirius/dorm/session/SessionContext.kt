package org.sirius.dorm.session
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

interface SessionContextProvider {
    fun getUser() : String
}

class SessionContext(val provider: SessionContextProvider) : SessionContextProvider {
    // implement

    override fun getUser(): String {
        return provider.getUser()
    }
}