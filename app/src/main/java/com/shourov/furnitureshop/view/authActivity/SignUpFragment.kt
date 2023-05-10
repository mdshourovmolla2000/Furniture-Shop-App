package com.shourov.furnitureshop.view.authActivity

import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.AppDatabase
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.databinding.FragmentSignUpBinding
import com.shourov.furnitureshop.repository.SignUpRepository
import com.shourov.furnitureshop.utils.KeyboardManager
import com.shourov.furnitureshop.utils.NetworkManager
import com.shourov.furnitureshop.utils.showErrorToast
import com.shourov.furnitureshop.utils.showSuccessToast
import com.shourov.furnitureshop.view_model.SignUpViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var dao: AppDao
    private lateinit var repository: SignUpRepository
    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        dao = AppDatabase.getDatabase(requireContext()).appDao()
        repository = SignUpRepository(dao)
        viewModel = ViewModelProvider(this, SignUpViewModelFactory(repository))[SignUpViewModel::class.java]

        binding.signUpButton.setOnClickListener { checkUser() }

        binding.signInTextview.setOnClickListener { findNavController().popBackStack() }

        return binding.root
    }

    private fun checkUser() {
        if (binding.nameEdittext.text.toString().trim().isEmpty()) {
            binding.nameEdittext.error = "Enter name"
            binding.nameEdittext.requestFocus()
            return
        }
        if (binding.emailEdittext.text.toString().trim().isEmpty()) {
            binding.emailEdittext.error = "Enter email"
            binding.emailEdittext.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEdittext.text.toString().trim()).matches()) {
            binding.emailEdittext.error = "Enter valid email"
            binding.emailEdittext.requestFocus()
            return
        }
        lifecycleScope.launch(Dispatchers.IO) {
            val count = viewModel.checkIfUserExists(binding.emailEdittext.text.toString().trim())

            withContext(Dispatchers.Main) {
                if (count > 0) {
                    binding.emailEdittext.error = "email already registered"
                    binding.emailEdittext.requestFocus()
                    return@withContext
                } else {
                    if (binding.passwordEdittext.text.toString().trim().isEmpty()) {
                        binding.passwordEdittext.error = "Enter password"
                        binding.passwordEdittext.requestFocus()
                        return@withContext
                    }
                    if (binding.passwordEdittext.text.toString().trim().length < 6) {
                        binding.passwordEdittext.error = "Must be 6 character"
                        binding.passwordEdittext.requestFocus()
                        return@withContext
                    }
                    KeyboardManager.hideKeyBoard(requireActivity())
                    if (NetworkManager.isInternetAvailable(requireContext())) {
                        binding.signUpButton.isClickable = false
                        insertUser()
                    } else {
                        requireContext().showErrorToast("No internet available")
                    }
                }
            }
        }

    }

    private fun insertUser() {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = viewModel.insertUser(UserTable(0, binding.nameEdittext.text.toString().trim(), binding.emailEdittext.text.toString().trim(), binding.passwordEdittext.text.toString().trim(), Uri.parse("android.resource://" + requireContext().packageName + "/" + R.drawable.user_profile_pic_placeholder_image).toString()))

            withContext(Dispatchers.Main) {
                if (result > 0) {
                    requireContext().showSuccessToast("Account created successfully")
                    binding.signUpButton.isClickable = true
                    findNavController().popBackStack()
                } else {
                    requireContext().showErrorToast("Something wrong")
                    binding.signUpButton.isClickable = true
                }
            }
        }
    }
}



class SignUpViewModelFactory(private val repository: SignUpRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = SignUpViewModel(repository) as T
}