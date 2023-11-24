package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shourov.furnitureshop.repository.ForgotPasswordRepository
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(private val repository: ForgotPasswordRepository): ViewModel() {
    fun forgotPassword(email: String?, callback: (message: String?) -> Unit) = viewModelScope.launch { repository.forgotPassword(email, callback) }
}