package com.shourov.furnitureshop.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.repository.SignUpRepository
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: SignUpRepository): ViewModel() {
    fun signUp(user: UserTable?, callback: (message: String?) -> Unit) = viewModelScope.launch { repository.signUp(user, callback) }
}