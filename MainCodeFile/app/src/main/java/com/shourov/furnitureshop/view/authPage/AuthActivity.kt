package com.shourov.furnitureshop.view.authPage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.repository.AuthRepository
import com.shourov.furnitureshop.utils.LoadingDialog
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.viewModel.AuthViewModel

class AuthActivity : AppCompatActivity() {

    lateinit var viewModel: AuthViewModel

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        SharedPref.init(this)
        loadingDialog = LoadingDialog(this)

        viewModel = ViewModelProvider(this, AuthViewModelFactory(AuthRepository()))[AuthViewModel::class.java]

        observerList()
    }

    private fun observerList() {
        viewModel.loadingDialogLiveData.observe(this) { it?.let { isLoading -> if (isLoading) loadingDialog.show() else loadingDialog.dismiss() } }

        viewModel.loadingDialogTextLiveData.observe(this) { loadingDialog.setText(it) }
    }
}





class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = AuthViewModel(repository) as T
}