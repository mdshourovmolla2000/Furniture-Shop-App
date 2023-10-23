package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.UserTable

class SignInRepository(private val dao: AppDao) {
    suspend fun checkIfUserExists(email: String?): Int = dao.checkIfUserExists(email)

    suspend fun checkIfUserIsValid(email: String?, password: String?): UserTable? = dao.checkIfUserIsValid(email, password)
}