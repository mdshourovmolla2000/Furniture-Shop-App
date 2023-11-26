package com.shourov.furnitureshop.view.profilePage.orderHistoryPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.shourov.furnitureshop.adapter.OrderHistoryViewPagerAdapter
import com.shourov.furnitureshop.databinding.FragmentOrderHistoryBinding

class OrderHistoryFragment : Fragment() {

    private lateinit var binding: FragmentOrderHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)

        binding.apply {
            backIcon.setOnClickListener { findNavController().popBackStack() }
            viewPager.adapter = OrderHistoryViewPagerAdapter(requireActivity())
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position ->
            when(position) {
                0-> tab.text = "Processing"
                1-> tab.text = "Shipped"
                2-> tab.text = "Delivered"
                else-> tab.text = "Processing"
            }
        }.attach()

        return binding.root
    }
}