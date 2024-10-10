package org.sirius.dorm.session
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

class DummySessionContextProvider(val userName: String) : SessionContextProvider {
    override fun getUser(): String {
        return userName
    }
}
