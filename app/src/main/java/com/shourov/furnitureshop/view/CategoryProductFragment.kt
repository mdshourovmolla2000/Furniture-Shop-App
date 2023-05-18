package com.shourov.furnitureshop.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.databinding.FragmentCategoryProductBinding

class CategoryProductFragment : Fragment() {

    private lateinit var binding: FragmentCategoryProductBinding

    private var categoryName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryProductBinding.inflate(inflater, container, false)

        categoryName = arguments?.getString("CATEGORY_NAME", "").toString()

        binding.backIcon.setOnClickListener { findNavController().popBackStack() }

        binding.titleTextview.text = categoryName

        return binding.root
    }
}