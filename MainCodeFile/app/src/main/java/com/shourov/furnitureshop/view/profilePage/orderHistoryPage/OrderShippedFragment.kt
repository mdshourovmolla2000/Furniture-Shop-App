package com.shourov.furnitureshop.view.profilePage.orderHistoryPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shourov.furnitureshop.databinding.FragmentOrderShippedBinding

class OrderShippedFragment : Fragment() {

    private lateinit var binding: FragmentOrderShippedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderShippedBinding.inflate(inflater, container, false)


        return binding.root
    }
}