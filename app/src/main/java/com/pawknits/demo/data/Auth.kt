package com.pawknits.demo.data

/**
 * Hard-coded demo accounts. There is no backend — this exists so test automation
 * can exercise happy-path and error-path logins.
 */
object DemoAccounts {
    const val VALID_USER = "doglover"
    const val LOCKED_USER = "locked"
    const val PASSWORD = "woof1234"
}

sealed interface LoginResult {
    /** HTTP status a real login API would return; used for the demo network request. */
    val httpStatus: Int

    data object Success : LoginResult {
        override val httpStatus = 200
    }

    data class Error(val message: String, override val httpStatus: Int) : LoginResult
}

fun authenticate(username: String, password: String): LoginResult {
    val user = username.trim().lowercase()
    return when {
        user.isEmpty() -> LoginResult.Error("Username is required", 400)
        password.isEmpty() -> LoginResult.Error("Password is required", 400)
        user == DemoAccounts.LOCKED_USER && password == DemoAccounts.PASSWORD ->
            LoginResult.Error("Sorry, this account has been locked out.", 403)
        user == DemoAccounts.VALID_USER && password == DemoAccounts.PASSWORD -> LoginResult.Success
        else -> LoginResult.Error("Username and password do not match any user", 401)
    }
}
