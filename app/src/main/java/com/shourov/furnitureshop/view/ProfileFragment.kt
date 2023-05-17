package com.shourov.furnitureshop.view

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.databinding.DialogLogoutBinding
import com.shourov.furnitureshop.databinding.FragmentProfileBinding
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.view.welcomeActivity.WelcomeActivity

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        SharedPref.init(requireContext())

        binding.backIcon.setOnClickListener { findNavController().popBackStack() }

        binding.logoutButton.setOnClickListener {
            logoutDialog()
        }

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

        dialogBinding.noButton.setOnClickListener { alertDialog.dismiss() }

        dialogBinding.yesButton.setOnClickListener {
            alertDialog.dismiss()
            SharedPref.write("IS_SIGNED_IN", "no")
            startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit)
            requireActivity().finish()
        }

        alertDialog.show()
    }
}