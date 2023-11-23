package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.UserTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignInRepository(private val dao: AppDao) {
    suspend fun signIn(email: String?, password: String?, callback: (data: UserTable?, message: String?) -> Unit) {
        checkIfUserExists(email, password, callback)
    }

    private suspend fun checkIfUserExists(email: String?, password: String?, callback: (data: UserTable?, message: String?) -> Unit) {
        val result = dao.checkIfUserExists(email)

        withContext(Dispatchers.Main) {
            if (result > 0) {
                validateUser(email, password, callback)
            } else {
                callback(null, "Email not registered")
                return@withContext
            }
        }
    }

    private suspend fun validateUser(email: String?, password: String?, callback: (data: UserTable?, message: String?) -> Unit) {
        val result = dao.checkIfUserIsValid(email, password)

        withContext(Dispatchers.Main) {
            if (result == null) {
                callback(null, "Password incorrect")
                return@withContext
            } else {
                callback(result, "Successfully signed in")
            }
        }
    }
}