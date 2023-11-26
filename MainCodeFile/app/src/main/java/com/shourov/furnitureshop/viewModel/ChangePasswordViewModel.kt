package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.repository.ChangePasswordRepository
import kotlinx.coroutines.launch

class ChangePasswordViewModel(private val repository: ChangePasswordRepository): ViewModel() {
    fun getUserInfo(id: Int?): LiveData<UserTable?> = repository.getUserInfo(id)

    fun changePassword(user: UserTable?, callback: (message: String?) -> Unit) = viewModelScope.launch { repository.changePassword(user, callback) }
}