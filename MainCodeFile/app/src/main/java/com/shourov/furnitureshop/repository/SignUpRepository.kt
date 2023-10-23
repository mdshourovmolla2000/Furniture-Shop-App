package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.UserTable

class SignUpRepository(private val dao: AppDao) {
    suspend fun checkIfUserExists(email: String?): Int = dao.checkIfUserExists(email)

    suspend fun insertUser(user: UserTable?): Long = dao.insertUser(user)
}