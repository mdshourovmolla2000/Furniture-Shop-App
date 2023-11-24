package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.database.AppDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForgotPasswordRepository(private val dao: AppDao) {
    suspend fun forgotPassword(email: String?, callback: (message: String?) -> Unit) {
        checkIfUserExists(email, callback)
    }

    private suspend fun checkIfUserExists(email: String?, callback: (message: String?) -> Unit) {
        val result = dao.checkIfUserExists(email)

        withContext(Dispatchers.Main) {
            if (result > 0) {
                callback("Recovery mail sent")
            } else {
                callback("Email not registered")
                return@withContext
            }
        }
    }
}