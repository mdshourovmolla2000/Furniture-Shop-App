package com.shourov.furnitureshop.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.repository.SignInRepository
import kotlinx.coroutines.launch

class SignInViewModel(private val repository: SignInRepository): ViewModel() {
    fun signIn(email: String?, password: String?, callback: (data: UserTable?, message: String?) -> Unit) = viewModelScope.launch { repository.signIn(email, password, callback) }
}