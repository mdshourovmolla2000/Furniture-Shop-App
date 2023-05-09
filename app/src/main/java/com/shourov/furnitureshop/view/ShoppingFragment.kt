package com.shourov.furnitureshop.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.databinding.FragmentShoppingBinding

class ShoppingFragment : Fragment() {

    private lateinit var binding: FragmentShoppingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShoppingBinding.inflate(inflater, container, false)

        binding.backIcon.setOnClickListener { findNavController().popBackStack() }

        return binding.root
    }
}