package com.shourov.furnitureshop.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.adapter.FavouriteListAdapter
import com.shourov.furnitureshop.databinding.FragmentFavouriteBinding
import com.shourov.furnitureshop.interfaces.FavouriteItemClickListener
import com.shourov.furnitureshop.model.FavouriteModel
import com.shourov.furnitureshop.repository.FavouriteRepository
import com.shourov.furnitureshop.view_model.FavouriteViewModel

class FavouriteFragment : Fragment(), FavouriteItemClickListener {

    private lateinit var binding: FragmentFavouriteBinding

    private lateinit var viewModel: FavouriteViewModel

    private val favouriteItemList: ArrayList<FavouriteModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)

        binding.backIcon.setOnClickListener { findNavController().popBackStack() }

        viewModel = ViewModelProvider(this, FavouriteViewModelFactory(FavouriteRepository()))[FavouriteViewModel::class.java]

        viewModel.getFavouriteData()

        observerList()

        binding.favouriteItemRecyclerview.adapter = FavouriteListAdapter(favouriteItemList, this@FavouriteFragment)


        binding.bottomNavigationHomeMenu.setOnClickListener { findNavController().navigate(R.id.action_favouriteFragment_to_homeFragment) }

        return binding.root
    }

    private fun observerList() {
        viewModel.favouriteLiveData.observe(viewLifecycleOwner) {
            favouriteItemList.clear()
            if (it.isEmpty()) {
                binding.favouriteItemRecyclerview.visibility = View.GONE
                binding.noItemLayout.visibility = View.VISIBLE
            } else {
                favouriteItemList.addAll(it)
                binding.favouriteItemRecyclerview.adapter?.notifyDataSetChanged()

                binding.noItemLayout.visibility = View.GONE
                binding.favouriteItemRecyclerview.visibility = View.VISIBLE
            }
        }
    }

    override fun onFavouriteItemClick(currentItem: FavouriteModel?, clickOn: String?) {
        when(clickOn) {
            "FAVOURITE_ICON" -> {
                favouriteItemList.remove(currentItem)
                binding.favouriteItemRecyclerview.adapter?.notifyDataSetChanged()

                if (favouriteItemList.isEmpty()) {
                    binding.favouriteItemRecyclerview.visibility = View.GONE
                    binding.noItemLayout.visibility = View.VISIBLE
                } else {
                    binding.noItemLayout.visibility = View.GONE
                    binding.favouriteItemRecyclerview.visibility = View.VISIBLE
                }
            }
            "MAIN_ITEM" -> findNavController().navigate(R.id.action_favouriteFragment_to_productDetailsFragment)
        }
    }
}




class FavouriteViewModelFactory(private val repository: FavouriteRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = FavouriteViewModel(repository) as T
}