package com.shourov.furnitureshop.view.profilePage.editProfilePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.application.BaseApplication.Companion.database
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.databinding.FragmentChangePasswordBinding
import com.shourov.furnitureshop.repository.ChangePasswordRepository
import com.shourov.furnitureshop.utils.KeyboardManager
import com.shourov.furnitureshop.utils.NetworkManager
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.utils.showErrorToast
import com.shourov.furnitureshop.utils.showSuccessToast
import com.shourov.furnitureshop.view.authPage.AuthActivity
import com.shourov.furnitureshop.viewModel.ChangePasswordViewModel

class ChangePasswordFragment : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding

    private lateinit var repository: ChangePasswordRepository
    private lateinit var viewModel: ChangePasswordViewModel

    private lateinit var user: UserTable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

        repository = ChangePasswordRepository(database.appDao())
        viewModel = ViewModelProvider(this, ChangePasswordViewModelFactory(repository))[ChangePasswordViewModel::class.java]

        observerList()

        binding.apply {
            backIcon.setOnClickListener { findNavController().popBackStack() }
            changePasswordButton.setOnClickListener { checkInput(it) }
        }

        return binding.root
    }

    private fun observerList() {
        viewModel.getUserInfo(SharedPref.read("CURRENT_USER_ID", "0")?.toInt()).observe(viewLifecycleOwner) {
            it?.let { user = it }
        }
    }

    private fun checkInput(view: View) {
        if (binding.currentPasswordEdittext.text.toString().isEmpty()) {
            binding.currentPasswordEdittext.error = "Enter current password"
            binding.currentPasswordEdittext.requestFocus()
            return
        }
        if (binding.currentPasswordEdittext.text.toString() != user.password) {
            requireContext().showErrorToast("Current password is incorrect")
            return
        }
        if (binding.newPasswordEdittext.text.toString().isEmpty()) {
            binding.newPasswordEdittext.error = "Enter new password"
            binding.newPasswordEdittext.requestFocus()
            return
        }
        if (binding.newPasswordEdittext.text.toString().length < 6) {
            binding.newPasswordEdittext.error = "Must be 6 character"
            binding.newPasswordEdittext.requestFocus()
            return
        }
        if (binding.confirmNewPasswordEdittext.text.toString() != binding.newPasswordEdittext.text.toString()) {
            requireContext().showErrorToast("Confirm password not matched")
            return
        }

        KeyboardManager.hideKeyBoard(requireContext(), view)
        if (NetworkManager.isInternetAvailable(requireContext())) {
            user.password = binding.confirmNewPasswordEdittext.text.toString()
            try { (activity as AuthActivity).viewModel.setLoadingDialogText("Changing password") } catch (_: Exception) { }
            try { (activity as AuthActivity).viewModel.setLoadingDialog(true) } catch (_: Exception) { }

            changePassword()
        } else {
            requireContext().showErrorToast("No internet available")
        }
    }

    private fun changePassword() {
        viewModel.changePassword(user) { message ->
            when(message) {
                "Password changed" -> {
                    requireContext().showSuccessToast(message)
                    findNavController().popBackStack()
                }
                "Something wrong" -> {
                    requireContext().showErrorToast(message)
                }
            }

            try { (activity as AuthActivity).viewModel.setLoadingDialog(false) } catch (_: Exception) { }
        }
    }
}




class ChangePasswordViewModelFactory(private val repository: ChangePasswordRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ChangePasswordViewModel(repository) as T
}