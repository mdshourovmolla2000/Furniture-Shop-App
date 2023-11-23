package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.repository.EditProfileRepository

class EditProfileViewModel(private val repository: EditProfileRepository): ViewModel() {
    fun getUserInfo(id: Int?): LiveData<UserTable?> = repository.getUserInfo(id)

    suspend fun updateUserInfo(user: UserTable): Int = repository.updateUserInfo(user)
}