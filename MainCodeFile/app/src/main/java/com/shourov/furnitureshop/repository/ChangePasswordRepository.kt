package com.shourov.furnitureshop.repository

import androidx.lifecycle.LiveData
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.UserTable

class ChangePasswordRepository(private val dao: AppDao) {
    fun getUserInfo(id: Int?): LiveData<UserTable?> = dao.getUserInfo(id)

    suspend fun updateUserInfo(user: UserTable): Int = dao.updateUser(user)
}