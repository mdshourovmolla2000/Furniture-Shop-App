package com.shourov.furnitureshop.view.profilePage

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
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.databinding.DialogLogoutBinding
import com.shourov.furnitureshop.databinding.FragmentProfileBinding
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.utils.loadImage
import com.shourov.furnitureshop.view.welcomePage.WelcomeActivity

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

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

        binding.apply {
            backIcon.setOnClickListener { findNavController().popBackStack() }

            editProfileButton.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment) }

            orderHistoryButton.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_orderHistoryFragment) }

            deliveryAddressButton.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_deliveryAddressFragment) }

            supportCenterButton.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_supportCenterFragment) }

            legalPolicyButton.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_policyFragment) }

            logoutButton.setOnClickListener { logoutDialog() }
        }

        updateView()

        return binding.root
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

    private fun updateView() {
        try {
            val response = Gson().fromJson(SharedPref.read("PROFILE_RESPONSE", ""), UserTable::class.java)
            binding.apply {
                if (response.profilePic.isNullOrEmpty()) {
                    profilePicImageview.loadImage(R.drawable.user_profile_pic_placeholder_image)
                } else {
                    profilePicImageview.loadImage(response.profilePic?.toUri())
                }
                userNameTextview.text = response.name
                userEmailTextview.text = response.email
            }
        } catch (_: Exception) { }
    }
}