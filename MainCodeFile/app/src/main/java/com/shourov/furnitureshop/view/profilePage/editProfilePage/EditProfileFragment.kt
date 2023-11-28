package com.shourov.furnitureshop.view.profilePage.editProfilePage

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.application.BaseApplication.Companion.database
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.databinding.FragmentEditProfileBinding
import com.shourov.furnitureshop.repository.EditProfileRepository
import com.shourov.furnitureshop.utils.KeyboardManager
import com.shourov.furnitureshop.utils.NetworkManager
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.utils.loadImage
import com.shourov.furnitureshop.utils.showErrorToast
import com.shourov.furnitureshop.utils.showInfoToast
import com.shourov.furnitureshop.utils.showSuccessToast
import com.shourov.furnitureshop.view.authPage.AuthActivity
import com.shourov.furnitureshop.viewModel.EditProfileViewModel

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding

    private lateinit var repository: EditProfileRepository
    private lateinit var viewModel: EditProfileViewModel

    private lateinit var user: UserTable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        repository = EditProfileRepository(database.appDao())
        viewModel = ViewModelProvider(this, EditProfileViewModelFactory(repository))[EditProfileViewModel::class.java]

        observerList()

        binding.apply {
            backIcon.setOnClickListener { findNavController().popBackStack() }
            profilePicImageview.setOnClickListener {
                ImagePicker.with(this@EditProfileFragment)
                    .crop(5f, 5f)
                    .compress(800)         //Final image size will be less than 0.8 MB(Optional)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }
            changePasswordTextview.setOnClickListener { findNavController().navigate(R.id.action_editProfileFragment_to_changePasswordFragment) }
            updateProfileButton.setOnClickListener { checkInput(it) }
        }

        return binding.root
    }

    private fun observerList() {
        viewModel.getUserInfo(SharedPref.read("CURRENT_USER_ID", "0")?.toInt()).observe(viewLifecycleOwner) {
            it?.let {
                user = it
                SharedPref.write("PROFILE_RESPONSE", Gson().toJson(it))
                binding.apply {
                    if (user.profilePic.isNullOrEmpty()) {
                        profilePicImageview.loadImage(R.drawable.user_profile_pic_placeholder_image)
                    } else {
                        profilePicImageview.loadImage(user.profilePic?.toUri())
                    }
                    nameEdittext.setText(it.name)
                }
            }
        }
    }

    private fun checkInput(view: View) {
        if (binding.nameEdittext.text.toString().trim().isEmpty()) {
            binding.nameEdittext.error = "Enter name"
            binding.nameEdittext.requestFocus()
            return
        }

        KeyboardManager.hideKeyBoard(requireContext(), view)
        if (NetworkManager.isInternetAvailable(requireContext())) {
            user.name = binding.nameEdittext.text.toString().trim()
            try { (activity as AuthActivity).viewModel.setLoadingDialogText("Updating profile") } catch (_: Exception) { }
            try { (activity as AuthActivity).viewModel.setLoadingDialog(true) } catch (_: Exception) { }

            updateProfile()
        } else {
            requireContext().showErrorToast("No internet available")
        }
    }

    private fun updateProfile() {
        viewModel.updateProfile(user) { message ->
            when(message) {
                "Updated" -> {
                    requireContext().showSuccessToast(message)
                }
                "Network error" -> {
                    requireContext().showErrorToast(message)
                }
                "Something wrong" -> {
                    requireContext().showErrorToast(message)
                }
            }

            try { (activity as AuthActivity).viewModel.setLoadingDialog(false) } catch (_: Exception) { }
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!
                    binding.profilePicImageview.loadImage(fileUri)
                    user.profilePic = fileUri.toString()
                }
                ImagePicker.RESULT_ERROR -> {
                    requireContext().showErrorToast(ImagePicker.getError(data))
                }
                else -> {
                    requireContext().showInfoToast("Cancelled")
                }
            }
        }
}



class EditProfileViewModelFactory(private val repository: EditProfileRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = EditProfileViewModel(repository) as T
}