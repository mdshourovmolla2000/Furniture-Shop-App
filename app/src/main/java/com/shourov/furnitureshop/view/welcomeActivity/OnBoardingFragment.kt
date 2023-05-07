package com.shourov.furnitureshop.view.welcomeActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.adapter.OnBoardingViewPagerAdapter
import com.shourov.furnitureshop.databinding.FragmentOnBoardingBinding
import com.shourov.furnitureshop.model.OnBoardingData
import com.shourov.furnitureshop.repository.OnBoardingRepository
import com.shourov.furnitureshop.view.authActivity.AuthActivity
import com.shourov.furnitureshop.view_model.OnBoardingViewModel

class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingBinding
    private val onBoardingData: MutableList<OnBoardingData> = ArrayList()
    private var onBoardingViewPagerAdapter: OnBoardingViewPagerAdapter? = null
    private var position = 0

    private lateinit var viewModel: OnBoardingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnBoardingBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, OnBoardingViewModelFactory(OnBoardingRepository()))[OnBoardingViewModel::class.java]

        viewModel.getOnBoardingData()

        observerList()

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

    private fun observerList() {
        viewModel.onBoardingLiveData.observe(viewLifecycleOwner){
            onBoardingData.clear()
            onBoardingData.addAll(it)
            binding.screenPager.adapter?.notifyDataSetChanged()
        }
    }


    private fun openNextActivity() {
        startActivity(Intent(requireActivity(), AuthActivity::class.java))
        requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit)
        requireActivity().finish()
    }
}




class OnBoardingViewModelFactory(private val repository: OnBoardingRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = OnBoardingViewModel(repository) as T
}