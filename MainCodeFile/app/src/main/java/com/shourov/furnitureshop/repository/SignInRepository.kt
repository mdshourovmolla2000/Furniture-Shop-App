package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.UserTable

class SignInRepository(private val dao: AppDao) {
    suspend fun signIn(email: String?, password: String?, callback: (data: UserTable?, message: String?) -> Unit) {
        checkIfUserExists(email, password, callback)
    }

    private suspend fun checkIfUserExists(email: String?, password: String?, callback: (data: UserTable?, message: String?) -> Unit) {
        val result = dao.checkIfUserExists(email)

        if (result > 0) {
            validateUser(email, password, callback)
        } else {
            callback(null, "Email not registered")
        }
    }

    private suspend fun validateUser(email: String?, password: String?, callback: (data: UserTable?, message: String?) -> Unit) {
        val result = dao.checkIfUserIsValid(email, password)

        if (result == null) {
            callback(null, "Password incorrect")
        } else {
            callback(result, "Successfully signed in")
        }
    }
}