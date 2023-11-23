package com.shourov.furnitureshop.view.welcomePage

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
import com.shourov.furnitureshop.model.OnBoardingModel
import com.shourov.furnitureshop.repository.OnBoardingRepository
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.view.authPage.AuthActivity
import com.shourov.furnitureshop.viewModel.OnBoardingViewModel

class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingBinding
    private val onBoardingData: ArrayList<OnBoardingModel> = ArrayList()
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

        binding.apply {
            screenPager.adapter = onBoardingViewPagerAdapter
            tabIndicator.setupWithViewPager(screenPager)
            position = screenPager.currentItem

            skipTextview.setOnClickListener { openNextActivity() }
            getStartedButton.setOnClickListener { openNextActivity() }
            nextButton.setOnClickListener {
                if (position < onBoardingData.size) {
                    position++
                    screenPager.currentItem = position
                }
            }

            tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    position = tab!!.position
                    if (position == onBoardingData.size - 1) {
                        nextSectionLayout.visibility = View.GONE
                        getStartedButton.visibility = View.VISIBLE
                    } else{
                        getStartedButton.visibility = View.GONE
                        nextSectionLayout.visibility = View.VISIBLE
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }

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
        SharedPref.write("IS_ONBOARDING_SCREEN_SHOWED", "yes")
        startActivity(Intent(requireActivity(), AuthActivity::class.java))
        requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit)
        requireActivity().finish()
    }
}




class OnBoardingViewModelFactory(private val repository: OnBoardingRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = OnBoardingViewModel(repository) as T
}