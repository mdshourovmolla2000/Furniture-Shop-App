package com.shourov.furnitureshop.view.otherPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.databinding.FragmentFullImageBinding
import com.shourov.furnitureshop.utils.loadImage

class FullImageFragment : Fragment() {

    private lateinit var binding: FragmentFullImageBinding
    private var imageUrl = R.drawable.app_icon

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFullImageBinding.inflate(inflater, container, false)

        imageUrl = arguments?.getInt("DATA")!!
        binding.fullImageImageview.loadImage(imageUrl)

        return binding.root
    }
}