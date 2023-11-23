package com.shourov.furnitureshop.view.authPage

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.application.BaseApplication.Companion.database
import com.shourov.furnitureshop.databinding.FragmentSignInBinding
import com.shourov.furnitureshop.repository.SignInRepository
import com.shourov.furnitureshop.utils.KeyboardManager
import com.shourov.furnitureshop.utils.NetworkManager
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.utils.showErrorToast
import com.shourov.furnitureshop.utils.showSuccessToast
import com.shourov.furnitureshop.view.MainActivity
import com.shourov.furnitureshop.viewModel.SignInViewModel

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var repository: SignInRepository
    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        repository = SignInRepository(database.appDao())
        viewModel = ViewModelProvider(this, SignInViewModelFactory(repository))[SignInViewModel::class.java]

        binding.apply {
            signInButton.setOnClickListener { checkUser(it) }
            signUpTextview.setOnClickListener {
                KeyboardManager.hideKeyBoard(requireContext(), it)
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }
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
        if (binding.passwordEdittext.text.toString().trim().isEmpty()) {
            binding.passwordEdittext.error = "Enter password"
            binding.passwordEdittext.requestFocus()
            KeyboardManager.showKeyboard(binding.passwordEdittext)
            return
        }
        if (binding.passwordEdittext.text.toString().trim().length < 6) {
            binding.passwordEdittext.error = "Must be 6 character"
            binding.passwordEdittext.requestFocus()
            KeyboardManager.showKeyboard(binding.passwordEdittext)
            return
        }

        KeyboardManager.hideKeyBoard(requireContext(), view)
        if (NetworkManager.isInternetAvailable(requireContext())) {
            try { (activity as AuthActivity).viewModel.setLoadingDialogText("Signing in") } catch (_: Exception) { }
            try { (activity as AuthActivity).viewModel.setLoadingDialog(true) } catch (_: Exception) { }

            signInUser(binding.emailEdittext.text.toString().trim(), binding.passwordEdittext.text.toString().trim())
        } else {
            requireContext().showErrorToast("No internet available")
        }
    }

    private fun signInUser(email: String?, password: String?) {
        viewModel.signIn(email, password) { data, message ->
            when(message) {
                "Email not registered" -> {
                    binding.emailEdittext.error = message
                    binding.emailEdittext.requestFocus()
                }
                "Password incorrect" -> {
                    binding.passwordEdittext.error = message
                    binding.passwordEdittext.requestFocus()
                }
                "Successfully signed in" -> {
                    requireContext().showSuccessToast(message)
                    SharedPref.write("CURRENT_USER_ID", data?.id.toString())
                    SharedPref.write("IS_SIGNED_IN", "yes")
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    val options = ActivityOptions.makeCustomAnimation(requireContext(), R.anim.enter, R.anim.exit)
                    startActivity(intent, options.toBundle())
                    requireActivity().finish()
                }
            }

            try { (activity as AuthActivity).viewModel.setLoadingDialog(false) } catch (_: Exception) { }
        }
    }
}





class SignInViewModelFactory(private val repository: SignInRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = SignInViewModel(repository) as T
}