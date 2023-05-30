package com.shourov.furnitureshop.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.AppDatabase
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.databinding.FragmentEditProfileBinding
import com.shourov.furnitureshop.repository.EditProfileRepository
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.utils.loadImage
import com.shourov.furnitureshop.view_model.EditProfileViewModel

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding

    private lateinit var dao: AppDao
    private lateinit var repository: EditProfileRepository
    private lateinit var viewModel: EditProfileViewModel

    private lateinit var user: UserTable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        SharedPref.init(requireContext())

        dao = AppDatabase.getDatabase(requireContext()).appDao()
        repository = EditProfileRepository(dao)
        viewModel = ViewModelProvider(this, EditProfileViewModelFactory(repository))[EditProfileViewModel::class.java]

        observerList()

        binding.backIcon.setOnClickListener { findNavController().popBackStack() }

        binding.changePasswordTextview.setOnClickListener { findNavController().navigate(R.id.action_editProfileFragment_to_changePasswordFragment) }

        binding.updateProfileButton.setOnClickListener {
            updateProfile()
        }

        return binding.root
    }

    private fun observerList() {
        viewModel.getUserInfo(SharedPref.read("CURRENT_USER_ID", "0")?.toInt()).observe(viewLifecycleOwner) {
            it?.let {
                user = it
                binding.profilePicImageview.loadImage(it.profile_pic.toUri())
                binding.nameEdittext.setText(it.name)
            }
        }
    }

    private fun updateProfile() {

    }
}



class EditProfileViewModelFactory(private val repository: EditProfileRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = EditProfileViewModel(repository) as T
}