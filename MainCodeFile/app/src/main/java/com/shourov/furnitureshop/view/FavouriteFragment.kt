package com.shourov.furnitureshop.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.adapter.FavouriteListAdapter
import com.shourov.furnitureshop.application.BaseApplication.Companion.database
import com.shourov.furnitureshop.database.tables.FavouriteTable
import com.shourov.furnitureshop.databinding.FragmentFavouriteBinding
import com.shourov.furnitureshop.interfaces.FavouriteItemClickListener
import com.shourov.furnitureshop.repository.FavouriteRepository
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.utils.loadImage
import com.shourov.furnitureshop.viewModel.FavouriteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteFragment : Fragment(), FavouriteItemClickListener {

    private lateinit var binding: FragmentFavouriteBinding

    private lateinit var repository: FavouriteRepository
    private lateinit var viewModel: FavouriteViewModel

    private val favouriteItemList = ArrayList<FavouriteTable>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)

        binding.backIcon.setOnClickListener { findNavController().popBackStack() }

        repository = FavouriteRepository(database.appDao())
        viewModel = ViewModelProvider(this, FavouriteViewModelFactory(repository))[FavouriteViewModel::class.java]

        observerList()

        binding.favouriteItemRecyclerview.adapter = FavouriteListAdapter(favouriteItemList, this@FavouriteFragment)

        return binding.root
    }

    private fun observerList() {
        viewModel.getFavouriteData(SharedPref.read("CURRENT_USER_ID", "0")?.toInt()).observe(viewLifecycleOwner) {
            favouriteItemList.clear()
            if (it.isNullOrEmpty()) {
                binding.apply {
                    favouriteItemRecyclerview.visibility = View.GONE
                    noItemLayout.visibility = View.VISIBLE
                }
            } else {
                favouriteItemList.addAll(it.reversed())

                binding.apply {
                    noItemLayout.visibility = View.GONE
                    favouriteItemRecyclerview.visibility = View.VISIBLE
                }
            }

            binding.favouriteItemRecyclerview.adapter?.notifyDataSetChanged()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onLoadFavouriteItem(currentProductId: String?, itemImageImageview: ImageView?, itemNameTextview: TextView?, itemPriceTextview: TextView?) {
        val currentProduct = viewModel.getProductDetails(currentProductId)
        currentProduct?.let {
            itemImageImageview?.loadImage(it.itemImage)
            itemNameTextview?.text = it.itemName
            itemPriceTextview?.text = "$${it.itemPrice}"
        }
    }

    override fun onFavouriteItemClick(currentItem: FavouriteTable, clickOn: String?) {
        when(clickOn) {
            "FAVOURITE_ICON" -> {
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.deleteFavourite(currentItem)
                }
            }
            "MAIN_ITEM" -> {
                val bundle = bundleOf(
                    "PRODUCT_ID" to (currentItem?.productId)
                )
                findNavController().navigate(R.id.action_favouriteFragment_to_productDetailsFragment, bundle)
            }
        }
    }
}




class FavouriteViewModelFactory(private val repository: FavouriteRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = FavouriteViewModel(repository) as T
}