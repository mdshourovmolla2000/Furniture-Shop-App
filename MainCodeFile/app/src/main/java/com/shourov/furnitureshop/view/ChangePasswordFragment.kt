package com.shourov.furnitureshop.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.AppDatabase
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.databinding.FragmentChangePasswordBinding
import com.shourov.furnitureshop.repository.ChangePasswordRepository
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.utils.showErrorToast
import com.shourov.furnitureshop.utils.showSuccessToast
import com.shourov.furnitureshop.view_model.ChangePasswordViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangePasswordFragment : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding

    private lateinit var dao: AppDao
    private lateinit var repository: ChangePasswordRepository
    private lateinit var viewModel: ChangePasswordViewModel

    private lateinit var user: UserTable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

        dao = AppDatabase.getDatabase(requireContext()).appDao()
        repository = ChangePasswordRepository(dao)
        viewModel = ViewModelProvider(this, ChangePasswordViewModelFactory(repository))[ChangePasswordViewModel::class.java]

        observerList()

        binding.backIcon.setOnClickListener { findNavController().popBackStack() }

        binding.changePasswordButton.setOnClickListener { changePassword() }

        return binding.root
    }

    private fun observerList() {
        viewModel.getUserInfo(SharedPref.read("CURRENT_USER_ID", "0")?.toInt()).observe(viewLifecycleOwner) {
            it?.let {
                user = it
            }
        }
    }

    private fun changePassword() {
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

        user.password = binding.confirmNewPasswordEdittext.text.toString()

        lifecycleScope.launch(Dispatchers.IO) {
            val result = viewModel.updateUserInfo(user)

            withContext(Dispatchers.Main) {
                if (result > 0) {
                    requireContext().showSuccessToast("Password changed")
                    findNavController().popBackStack()
                } else {
                    requireContext().showErrorToast("Something wrong")
                }
            }
        }
    }
}



class ChangePasswordViewModelFactory(private val repository: ChangePasswordRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ChangePasswordViewModel(repository) as T
}