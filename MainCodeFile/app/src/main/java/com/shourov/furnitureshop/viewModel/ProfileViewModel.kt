package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.repository.ProfileRepository

class ProfileViewModel(private val repository: ProfileRepository): ViewModel() {
    fun getUserInfo(id: Int?): LiveData<UserTable?> = repository.getUserInfo(id)
}