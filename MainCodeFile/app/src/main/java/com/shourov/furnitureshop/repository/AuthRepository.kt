package com.shourov.furnitureshop.repository

class AuthRepository {
    fun getLoadingDialog(isLoading: Boolean?) = isLoading

    fun getLoadingDialogText(text: String?) = text
}