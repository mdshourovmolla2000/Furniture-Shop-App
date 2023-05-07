package com.shourov.furnitureshop.view.authActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.databinding.FragmentSignInBinding
import com.shourov.furnitureshop.view.MainActivity

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.signInButton.setOnClickListener {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit)
            requireActivity().finish()
        }

        binding.signUpTextview.setOnClickListener { findNavController().navigate(R.id.action_signInFragment_to_signUpFragment) }

        return binding.root
    }
}