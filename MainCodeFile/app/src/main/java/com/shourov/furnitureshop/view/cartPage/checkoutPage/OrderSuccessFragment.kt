package com.shourov.furnitureshop.view.cartPage.checkoutPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.databinding.BottomSheetOrderSuccessBinding
import com.shourov.furnitureshop.databinding.FragmentOrderSuccessBinding

class OrderSuccessFragment : Fragment() {

    private lateinit var binding: FragmentOrderSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderSuccessBinding.inflate(inflater, container, false)

        showBottomSheet()

        binding.apply {
            backIcon.setOnClickListener { findNavController().navigate(R.id.action_orderSuccessFragment_to_homeFragment) }

            backHomeButton.setOnClickListener { findNavController().navigate(R.id.action_orderSuccessFragment_to_homeFragment) }
        }

        return binding.root
    }

    private fun showBottomSheet() {
        val bottomSheet = BottomSheetDialog(requireContext())
        val bottomSheetBinding = BottomSheetOrderSuccessBinding.inflate(LayoutInflater.from(requireContext()))

        bottomSheet.setContentView(bottomSheetBinding.root)
        
        bottomSheetBinding.successIconImageview.setImageResource(R.drawable.order_success_icon)

        bottomSheetBinding.backHomeButton.setOnClickListener {
            findNavController().navigate(R.id.action_orderSuccessFragment_to_homeFragment)
            bottomSheet.dismiss()
        }

        bottomSheet.show()
    }
}