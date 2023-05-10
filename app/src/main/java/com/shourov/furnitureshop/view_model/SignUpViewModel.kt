package com.shourov.furnitureshop.view_model

import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.repository.SignUpRepository

class SignUpViewModel(private val repository: SignUpRepository): ViewModel() {
    suspend fun checkIfUserExists(email: String?): Int = repository.checkIfUserExists(email)

    suspend fun insertUser(user: UserTable): Long = repository.insertUser(user)
}