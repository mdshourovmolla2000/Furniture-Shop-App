package com.shourov.furnitureshop.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.AppDatabase
import com.shourov.furnitureshop.databinding.FragmentProductDetailsBinding
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.repository.ProductDetailsRepository
import com.shourov.furnitureshop.view_model.ProductDetailsViewModel
import java.text.DecimalFormat

class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding

    private lateinit var dao: AppDao
    private lateinit var repository: ProductDetailsRepository
    private lateinit var viewModel: ProductDetailsViewModel

    private var productId = "1"
    private var productImageList = ArrayList<SlideModel>()

    private lateinit var currentProduct: ProductModel
    private var productQuantity = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)

        productId = arguments?.getString("PRODUCT_ID").toString()

        binding.backIcon.setOnClickListener { findNavController().popBackStack() }

        dao = AppDatabase.getDatabase(requireContext()).appDao()
        repository = ProductDetailsRepository(dao)
        viewModel = ViewModelProvider(this, ProductDetailsViewModelFactory(repository))[ProductDetailsViewModel::class.java]

        viewModel.getProductImages(productId)
        viewModel.getProductDetails(productId)

        observerList()

        binding.itemQuantityPlusIcon.setOnClickListener {
            productQuantity++
            viewModel.getTotalAmount(currentProduct.itemPrice, productQuantity)
        }

        binding.itemQuantityMinusIcon.setOnClickListener {
            if (productQuantity > 1) {
                productQuantity--
                viewModel.getTotalAmount(currentProduct.itemPrice, productQuantity)
            }
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun observerList() {
        viewModel.productImageLiveData.observe(viewLifecycleOwner) {
            for (item in it) {
                productImageList.add(SlideModel(item?.productImage))
            }
            binding.productImageSlider.setImageList(productImageList, ScaleTypes.FIT)
        }

        viewModel.productDetailsLiveData.observe(viewLifecycleOwner) {
            it?.let {
                currentProduct = it
                binding.productNameTextview.text = it.itemName
                binding.productPriceTextview.text = "$${it.itemPrice}"
                binding.productDescriptionTextview.text = it.itemDescription
                binding.itemCountTextview.text = productQuantity.toString()

                viewModel.getTotalAmount(it.itemPrice, productQuantity)
            }
        }

        viewModel.totalAmountLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.itemCountTextview.text = productQuantity.toString()
                binding.totalPriceTextview.text = "$${DecimalFormat("#.##").format(it)}"
            }
        }
    }
}





class ProductDetailsViewModelFactory(private val repository: ProductDetailsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ProductDetailsViewModel(repository) as T
}