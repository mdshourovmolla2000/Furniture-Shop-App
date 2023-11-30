package com.shourov.furnitureshop.view.profilePage.orderHistoryPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shourov.furnitureshop.databinding.FragmentOrderDeliveredBinding

class OrderDeliveredFragment : Fragment() {

    private lateinit var binding: FragmentOrderDeliveredBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderDeliveredBinding.inflate(inflater, container, false)


        return binding.root
    }
}