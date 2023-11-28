package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.database.AppDao
import java.io.IOException
import java.net.SocketException

class ForgotPasswordRepository(private val dao: AppDao) {
    suspend fun forgotPassword(email: String?, callback: (message: String?) -> Unit) {
        checkIfUserExists(email, callback)
    }

    private suspend fun checkIfUserExists(email: String?, callback: (message: String?) -> Unit) {
        try {
            val result = dao.checkIfUserExists(email)

            if (result > 0) {
                callback("Recovery mail sent")
            } else {
                callback("Email not registered")
            }
        } catch (e: IOException) {
            callback("Network error")
        } catch (e: SocketException) {
            callback("Network error")
        } catch (e: Exception) {
            callback("Something wrong")
        }
    }
}