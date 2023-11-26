package com.shourov.furnitureshop.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.application.BaseApplication.Companion.database
import com.shourov.furnitureshop.database.tables.FavouriteTable
import com.shourov.furnitureshop.database.tables.ShoppingTable
import com.shourov.furnitureshop.databinding.FragmentProductDetailsBinding
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.repository.ProductDetailsRepository
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.utils.loadImage
import com.shourov.furnitureshop.viewModel.ProductDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding

    private lateinit var repository: ProductDetailsRepository
    private lateinit var viewModel: ProductDetailsViewModel

    private var productId = "1"
    private var productImageList = ArrayList<SlideModel>()

    private lateinit var currentProduct: ProductModel
    private var productQuantity = 1
    private var productInFavourite = false
    private var productInShopping = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)

        productId = arguments?.getString("PRODUCT_ID").toString()

        binding.backIcon.setOnClickListener { findNavController().popBackStack() }

        repository = ProductDetailsRepository(database.appDao())
        viewModel = ViewModelProvider(this, ProductDetailsViewModelFactory(repository))[ProductDetailsViewModel::class.java]

        viewModel.getProductImages(productId)
        viewModel.getProductDetails(productId)

        observerList()

        binding.apply {
            favouriteIcon.setOnClickListener {
                if (productInFavourite) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.deleteFavouriteById(SharedPref.read("CURRENT_USER_ID", "0")?.toInt(), productId)
                    }
                } else {
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.insertFavourite(FavouriteTable(0, productId, SharedPref.read("CURRENT_USER_ID", "0")?.toInt()))
                    }
                }
            }

            itemQuantityPlusIcon.setOnClickListener {
                productQuantity++
                viewModel.getTotalAmount(currentProduct.itemPrice, productQuantity)
            }

            itemQuantityMinusIcon.setOnClickListener {
                if (productQuantity > 1) {
                    productQuantity--
                    viewModel.getTotalAmount(currentProduct.itemPrice, productQuantity)
                }
            }

            addToCartButton.setOnClickListener{
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.insertShopping(ShoppingTable(0, productId, currentProduct.itemImage, currentProduct.itemName, currentProduct.itemCompanyName, currentProduct.itemPrice, SharedPref.read("CURRENT_USER_ID", "0")?.toInt(), productQuantity, false))
                }
            }

            removeFromCartButton.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.deleteShoppingById(SharedPref.read("CURRENT_USER_ID", "0")?.toInt(), productId)
                }
            }

            goToCartButton.setOnClickListener {
                findNavController().navigate(R.id.action_productDetailsFragment_to_cartFragment)
            }
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun observerList() {
        viewModel.productImageLiveData.observe(viewLifecycleOwner) {
            for (item in it) { productImageList.add(SlideModel(item?.productImage)) }
            binding.productImageSlider.setImageList(productImageList, ScaleTypes.FIT)
        }

        viewModel.productDetailsLiveData.observe(viewLifecycleOwner) {
            it?.let {
                currentProduct = it
                binding.apply {
                    productNameTextview.text = it.itemName
                    productPriceTextview.text = "$${it.itemPrice}"
                    productDescriptionTextview.text = it.itemDescription
                    itemCountTextview.text = productQuantity.toString()
                }

                viewModel.getTotalAmount(it.itemPrice, productQuantity)
            }
        }

        viewModel.totalAmountLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.apply {
                    itemCountTextview.text = productQuantity.toString()
                    totalPriceTextview.text = "$${DecimalFormat("#.##").format(it)}"
                }
            }
        }

        viewModel.checkIfProductIsInFavourite(SharedPref.read("CURRENT_USER_ID", "0")?.toInt(), productId).observe(viewLifecycleOwner) {
            productInFavourite = it > 0

            if (productInFavourite) {
                binding.favouriteIconImageview.loadImage(R.drawable.favourite_icon_fill)
            } else {
                binding.favouriteIconImageview.loadImage(R.drawable.bottom_navigation_menu_favourite_icon)
            }
        }

        viewModel.checkIfProductIsInShopping(SharedPref.read("CURRENT_USER_ID", "0")?.toInt(), productId).observe(viewLifecycleOwner) {
            productInShopping = it > 0
            binding.apply {
                if (productInShopping) {
                    addToCartLayout.visibility = View.GONE
                    removeFromCartButton.visibility = View.VISIBLE
                    goToCartButton.visibility = View.VISIBLE
                } else {
                    removeFromCartButton.visibility = View.GONE
                    goToCartButton.visibility = View.GONE
                    addToCartLayout.visibility = View.VISIBLE
                }
            }
        }
    }
}





class ProductDetailsViewModelFactory(private val repository: ProductDetailsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ProductDetailsViewModel(repository) as T
}