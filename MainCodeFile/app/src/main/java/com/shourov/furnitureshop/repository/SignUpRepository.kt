package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.UserTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignUpRepository(private val dao: AppDao) {
    suspend fun signUp(user: UserTable?, callback: (message: String?) -> Unit) {
        checkIfUserExists(user, callback)
    }

    private suspend fun checkIfUserExists(user: UserTable?, callback: (message: String?) -> Unit) {
        val result = dao.checkIfUserExists(user?.email)

        withContext(Dispatchers.Main) {
            if (result > 0) {
                callback("Email already registered")
                return@withContext
            } else {
                insertUser(user, callback)
            }
        }
    }

    private suspend fun insertUser(user: UserTable?, callback: (message: String?) -> Unit) {
        val result = dao.insertUser(user!!)

        withContext(Dispatchers.Main) {
            if (result > 0) {
                callback("Account created successfully")
                return@withContext
            } else {
                callback("Something wrong")
            }
        }
    }
}