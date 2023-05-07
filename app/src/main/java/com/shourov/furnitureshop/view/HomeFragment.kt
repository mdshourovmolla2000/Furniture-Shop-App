package com.shourov.furnitureshop.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shourov.furnitureshop.adapter.SpecialOffersListAdapter
import com.shourov.furnitureshop.databinding.FragmentHomeBinding
import com.shourov.furnitureshop.model.SpecialOfferData
import com.shourov.furnitureshop.repository.HomeRepository
import com.shourov.furnitureshop.view_model.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var viewModel: HomeViewModel

    private val specialOfferItemsList: ArrayList<SpecialOfferData> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, HomeViewModelFactory(HomeRepository()))[HomeViewModel::class.java]

        viewModel.getSpecialOfferLiveData()

        observerList()

        binding.specialOfferRecyclerview.apply {
            adapter = SpecialOffersListAdapter(specialOfferItemsList)
        }

        return binding.root
    }

    private fun observerList() {
        viewModel.specialOfferLiveData.observe(viewLifecycleOwner) {
            specialOfferItemsList.clear()
            specialOfferItemsList.addAll(it)
            binding.specialOfferRecyclerview.adapter?.notifyDataSetChanged()
        }
    }
}




class HomeViewModelFactory(private val repository: HomeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel(repository) as T
}