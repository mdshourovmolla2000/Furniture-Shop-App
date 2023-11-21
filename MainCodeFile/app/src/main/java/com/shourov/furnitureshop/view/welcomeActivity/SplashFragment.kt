package com.shourov.furnitureshop.view.welcomeActivity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.databinding.FragmentSplashBinding
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.view.MainActivity
import com.shourov.furnitureshop.view.authActivity.AuthActivity

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding

    private val waitingTime = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        SharedPref.init(requireContext())

        Handler(Looper.getMainLooper()).postDelayed({
            if (SharedPref.read("IS_SIGNED_IN", "no") == "yes") {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                val options = ActivityOptions.makeCustomAnimation(requireContext(), R.anim.enter, R.anim.exit)
                startActivity(intent, options.toBundle())
                requireActivity().finish()
            } else {
                if (SharedPref.read("IS_ONBOARDING_SCREEN_SHOWED", "no") == "yes") {
                    val intent = Intent(requireActivity(), AuthActivity::class.java)
                    val options = ActivityOptions.makeCustomAnimation(requireContext(), R.anim.enter, R.anim.exit)
                    startActivity(intent, options.toBundle())
                    requireActivity().finish()
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_onBoardingFragment)
                }
            }
        }, waitingTime * 1000L)

        return binding.root
    }
}