package org.sirius.dorm.session
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.springframework.stereotype.Component

interface SessionContextProvider {
    fun getUser() : String
}

class DefaultSessionContextProvider : SessionContextProvider {
    override fun getUser(): String {
        return "me"
    }

}

// TODO...

@Component
class SessionContext : SessionContextProvider{
    val provider = DefaultSessionContextProvider()

    // implement

    override fun getUser(): String {
        return provider.getUser()
    }
}