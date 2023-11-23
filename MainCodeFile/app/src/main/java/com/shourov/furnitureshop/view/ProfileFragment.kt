package com.shourov.furnitureshop.view

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
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
import com.shourov.furnitureshop.application.BaseApplication.Companion.database
import com.shourov.furnitureshop.databinding.DialogLogoutBinding
import com.shourov.furnitureshop.databinding.FragmentProfileBinding
import com.shourov.furnitureshop.repository.ProfileRepository
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.utils.loadImage
import com.shourov.furnitureshop.view.welcomePage.WelcomeActivity
import com.shourov.furnitureshop.viewModel.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private lateinit var repository: ProfileRepository
    private lateinit var viewModel: ProfileViewModel
    private var scrollPosition = 0

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        scrollPosition = binding.mainScrollView.scrollY
        outState.putInt("SCROLL_POSITION", scrollPosition)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            try {
                scrollPosition = savedInstanceState.getInt("SCROLL_POSITION")
                binding.mainScrollView.post { binding.mainScrollView.scrollTo(0, scrollPosition) }
            } catch (_: Exception) {}
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        repository = ProfileRepository(database.appDao())
        viewModel = ViewModelProvider(this, ProfileViewModelFactory(repository))[ProfileViewModel::class.java]

        observerList()

        binding.apply {
            backIcon.setOnClickListener { findNavController().popBackStack() }

            editProfileButton.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment) }

            deliveryAddressButton.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_deliveryAddressFragment) }

            supportCenterButton.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_supportCenterFragment) }

            legalPolicyButton.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_policyFragment) }

            logoutButton.setOnClickListener { logoutDialog() }
        }

        return binding.root
    }

    private fun observerList() {
        viewModel.getUserInfo(SharedPref.read("CURRENT_USER_ID", "0")?.toInt()).observe(viewLifecycleOwner) {
            it?.let {
                binding.apply {
                    profilePicImageview.loadImage(it.profilePic?.toUri())
                    userNameTextview.text = it.name
                    userEmailTextview.text = it.email
                }
            }
        }
    }

    private fun logoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogBinding = DialogLogoutBinding.inflate(layoutInflater)

        builder.setView(dialogBinding.root)
        builder.setCancelable(true)

        val alertDialog = builder.create()

        //make transparent to default dialog
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(0))

        dialogBinding.apply {
            noButton.setOnClickListener { alertDialog.dismiss() }

            yesButton.setOnClickListener {
                alertDialog.dismiss()
                SharedPref.write("IS_SIGNED_IN", "no")
                val intent = Intent(requireActivity(), WelcomeActivity::class.java)
                val options = ActivityOptions.makeCustomAnimation(requireContext(), R.anim.enter, R.anim.exit)
                startActivity(intent, options.toBundle())
                requireActivity().finish()
            }
        }

        alertDialog.show()
    }
}





class ProfileViewModelFactory(private val repository: ProfileRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ProfileViewModel(repository) as T
}