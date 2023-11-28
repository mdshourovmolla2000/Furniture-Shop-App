package com.shourov.furnitureshop.repository

import androidx.lifecycle.LiveData
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.UserTable
import java.io.IOException
import java.net.SocketException

class ChangePasswordRepository(private val dao: AppDao) {
    fun getUserInfo(id: Int?): LiveData<UserTable?> = dao.getUserInfo(id)

    suspend fun changePassword(user: UserTable?, callback: (message: String?) -> Unit) {
        try {
            val result = dao.updateUser(user!!)

            if (result > 0) {
                callback("Password changed")
            } else {
                callback("Something wrong")
            }
        }
        catch (e: IOException) { callback("Network error") }
        catch (e: SocketException) { callback("Network error") }
        catch (e: Exception) { callback("Something wrong") }
    }
}