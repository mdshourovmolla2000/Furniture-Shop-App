package com.shourov.furnitureshop.view.authPage

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.application.BaseApplication.Companion.database
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.databinding.FragmentSignUpBinding
import com.shourov.furnitureshop.repository.SignUpRepository
import com.shourov.furnitureshop.utils.KeyboardManager
import com.shourov.furnitureshop.utils.NetworkManager
import com.shourov.furnitureshop.utils.showErrorToast
import com.shourov.furnitureshop.utils.showSuccessToast
import com.shourov.furnitureshop.viewModel.SignUpViewModel

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var repository: SignUpRepository
    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        repository = SignUpRepository(database.appDao())
        viewModel = ViewModelProvider(this, SignUpViewModelFactory(repository))[SignUpViewModel::class.java]

        binding.apply {
            signUpButton.setOnClickListener { checkUser(it) }
            signInTextview.setOnClickListener { findNavController().popBackStack() }
        }

        return binding.root
    }

    private fun checkUser(view: View) {
        if (binding.nameEdittext.text.toString().trim().isEmpty()) {
            binding.nameEdittext.error = "Enter name"
            binding.nameEdittext.requestFocus()
            KeyboardManager.showKeyboard(binding.nameEdittext)
            return
        }
        if (binding.emailEdittext.text.toString().trim().isEmpty()) {
            binding.emailEdittext.error = "Enter email"
            binding.emailEdittext.requestFocus()
            KeyboardManager.showKeyboard(binding.emailEdittext)
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEdittext.text.toString().trim()).matches()) {
            binding.emailEdittext.error = "Enter valid email"
            binding.emailEdittext.requestFocus()
            KeyboardManager.showKeyboard(binding.emailEdittext)
            return
        }
        if (binding.passwordEdittext.text.toString().isEmpty()) {
            binding.passwordEdittext.error = "Enter password"
            binding.passwordEdittext.requestFocus()
            KeyboardManager.showKeyboard(binding.passwordEdittext)
            return
        }
        if (binding.passwordEdittext.text.toString().length < 6) {
            binding.passwordEdittext.error = "Must be 6 character"
            binding.passwordEdittext.requestFocus()
            KeyboardManager.showKeyboard(binding.passwordEdittext)
            return
        }

        KeyboardManager.hideKeyBoard(requireContext(), view)
        if (NetworkManager.isInternetAvailable(requireContext())) {
            try { (activity as AuthActivity).viewModel.setLoadingDialogText("Creating account") } catch (_: Exception) { }
            try { (activity as AuthActivity).viewModel.setLoadingDialog(true) } catch (_: Exception) { }

            signUpUser(UserTable(0, binding.nameEdittext.text.toString().trim(), binding.emailEdittext.text.toString().trim(), binding.passwordEdittext.text.toString().trim(), ""))
        } else {
            requireContext().showErrorToast("No internet available")
        }
    }

    private fun signUpUser(user: UserTable?) {
        viewModel.signUp(user) { message ->
            when(message) {
                "Email already registered" -> {
                    binding.emailEdittext.error = message
                    binding.emailEdittext.requestFocus()
                }
                "Account created successfully" -> {
                    requireContext().showSuccessToast(message)
                    findNavController().popBackStack()
                }
                "Network error" -> requireContext().showErrorToast(message)
                "Something wrong" -> requireContext().showErrorToast(message)
            }

            try { (activity as AuthActivity).viewModel.setLoadingDialog(false) } catch (_: Exception) { }
        }
    }
}





class SignUpViewModelFactory(private val repository: SignUpRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = SignUpViewModel(repository) as T
}