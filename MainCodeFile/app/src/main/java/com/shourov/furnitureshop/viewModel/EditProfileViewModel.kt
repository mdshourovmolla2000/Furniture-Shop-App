package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.repository.EditProfileRepository
import kotlinx.coroutines.launch

class EditProfileViewModel(private val repository: EditProfileRepository): ViewModel() {
    fun getUserInfo(id: Int?): LiveData<UserTable?> = repository.getUserInfo(id)

    fun updateProfile(user: UserTable?, callback: (message: String?) -> Unit) = viewModelScope.launch { repository.updateProfile(user, callback) }
}