package com.c241ps319.patera.data.local

import com.c241ps319.patera.data.ResultState
import com.c241ps319.patera.data.model.LoggedInUser
import java.io.IOException
import java.util.UUID

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): ResultState<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(UUID.randomUUID().toString(), "Jane Doe")
            return ResultState.Success(fakeUser)
        } catch (e: Throwable) {
            return ResultState.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}