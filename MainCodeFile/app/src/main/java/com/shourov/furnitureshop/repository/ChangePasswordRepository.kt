package com.shourov.furnitureshop.repository

import androidx.lifecycle.LiveData
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.UserTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChangePasswordRepository(private val dao: AppDao) {
    fun getUserInfo(id: Int?): LiveData<UserTable?> = dao.getUserInfo(id)

    suspend fun changePassword(user: UserTable?, callback: (message: String?) -> Unit) {
        val result = dao.updateUser(user!!)

        withContext(Dispatchers.Main) {
            if (result > 0) {
                callback("Password changed")
            } else {
                callback("Something wrong")
            }
        }
    }
}