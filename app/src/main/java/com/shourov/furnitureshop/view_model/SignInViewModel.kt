package com.shourov.furnitureshop.view_model

import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.repository.SignInRepository

class SignInViewModel(private val repository: SignInRepository): ViewModel() {
    suspend fun checkIfUserExists(email: String?): Int = repository.checkIfUserExists(email)

    suspend fun checkIfUserIsValid(email: String?, password: String?): UserTable? = repository.checkIfUserIsValid(email, password)
}