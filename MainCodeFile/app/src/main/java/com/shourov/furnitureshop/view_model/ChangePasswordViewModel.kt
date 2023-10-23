package com.shourov.furnitureshop.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.repository.ChangePasswordRepository

class ChangePasswordViewModel(private val repository: ChangePasswordRepository): ViewModel() {
    fun getUserInfo(id: Int?): LiveData<UserTable?> = repository.getUserInfo(id)

    suspend fun updateUserInfo(user: UserTable?): Int = repository.updateUserInfo(user)
}