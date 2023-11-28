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
import com.shourov.furnitureshop.databinding.FragmentForgotPasswordBinding
import com.shourov.furnitureshop.repository.ForgotPasswordRepository
import com.shourov.furnitureshop.utils.KeyboardManager
import com.shourov.furnitureshop.utils.NetworkManager
import com.shourov.furnitureshop.utils.showErrorToast
import com.shourov.furnitureshop.utils.showSuccessToast
import com.shourov.furnitureshop.viewModel.ForgotPasswordViewModel

class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var repository: ForgotPasswordRepository
    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)

        repository = ForgotPasswordRepository(database.appDao())
        viewModel = ViewModelProvider(this, ForgotPasswordViewModelFactory(repository))[ForgotPasswordViewModel::class.java]

        binding.apply {
            backIcon.setOnClickListener { findNavController().popBackStack() }
            continueButton.setOnClickListener { checkUser(it) }
        }

        return binding.root
    }

    private fun checkUser(view: View) {
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

        KeyboardManager.hideKeyBoard(requireContext(), view)
        if (NetworkManager.isInternetAvailable(requireContext())) {
            try { (activity as AuthActivity).viewModel.setLoadingDialogText("Processing") } catch (_: Exception) { }
            try { (activity as AuthActivity).viewModel.setLoadingDialog(true) } catch (_: Exception) { }

            forgotPassword(binding.emailEdittext.text.toString().trim())
        } else {
            requireContext().showErrorToast("No internet available")
        }
    }

    private fun forgotPassword(email: String?) {
        viewModel.forgotPassword(email) { message ->
            when(message) {
                "Email not registered" -> {
                    binding.emailEdittext.error = message
                    binding.emailEdittext.requestFocus()
                }
                "Network error" -> {
                    requireContext().showErrorToast(message)
                }
                "Something wrong" -> {
                    requireContext().showErrorToast(message)
                }

                "Recovery mail sent" -> requireContext().showSuccessToast(message)
            }

            try { (activity as AuthActivity).viewModel.setLoadingDialog(false) } catch (_: Exception) { }
        }
    }
}




class ForgotPasswordViewModelFactory(private val repository: ForgotPasswordRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ForgotPasswordViewModel(repository) as T
}