package com.shourov.furnitureshop.view.welcomeActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.adapter.OnBoardingViewPagerAdapter
import com.shourov.furnitureshop.databinding.FragmentOnBoardingBinding
import com.shourov.furnitureshop.model.OnBoardingData
import com.shourov.furnitureshop.utils.DemoData
import com.shourov.furnitureshop.view.MainActivity

class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingBinding
    private val onBoardingData: MutableList<OnBoardingData> = ArrayList()
    private var onBoardingViewPagerAdapter: OnBoardingViewPagerAdapter? = null
    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnBoardingBinding.inflate(inflater, container, false)

        onBoardingData.addAll(DemoData().onBoardingData())

        onBoardingViewPagerAdapter = OnBoardingViewPagerAdapter(onBoardingData)
        binding.screenPager.adapter = onBoardingViewPagerAdapter
        binding.tabIndicator.setupWithViewPager(binding.screenPager)

        position = binding.screenPager.currentItem

        binding.skipTextview.setOnClickListener { openNextActivity() }

        binding.getStartedButton.setOnClickListener { openNextActivity() }

        binding.nextButton.setOnClickListener {
            if (position < onBoardingData.size) {
                position++
                binding.screenPager.currentItem = position
            }

            if (position == onBoardingData.size){
                openNextActivity()
            }
        }

        binding.tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                position = tab!!.position
                if (position == onBoardingData.size - 1) {
                    binding.nextSectionLayout.visibility = View.GONE
                    binding.getStartedButton.visibility = View.VISIBLE
                } else{
                    binding.getStartedButton.visibility = View.GONE
                    binding.nextSectionLayout.visibility = View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        return binding.root
    }

    private fun openNextActivity() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
        requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit)
    }
}