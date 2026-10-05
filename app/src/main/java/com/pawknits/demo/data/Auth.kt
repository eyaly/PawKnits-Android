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
    data object Success : LoginResult
    data class Error(val message: String) : LoginResult
}

fun authenticate(username: String, password: String): LoginResult {
    val user = username.trim().lowercase()
    return when {
        user.isEmpty() -> LoginResult.Error("Username is required")
        password.isEmpty() -> LoginResult.Error("Password is required")
        user == DemoAccounts.LOCKED_USER && password == DemoAccounts.PASSWORD ->
            LoginResult.Error("Sorry, this account has been locked out.")
        user == DemoAccounts.VALID_USER && password == DemoAccounts.PASSWORD -> LoginResult.Success
        else -> LoginResult.Error("Username and password do not match any user")
    }
}
