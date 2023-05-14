package com.shourov.furnitureshop.view

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
import com.shourov.furnitureshop.repository.ProductDetailsRepository
import com.shourov.furnitureshop.view_model.ProductDetailsViewModel

class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding

    private lateinit var dao: AppDao
    private lateinit var repository: ProductDetailsRepository
    private lateinit var viewModel: ProductDetailsViewModel

    private var productId = "1"
    private var productImageList = ArrayList<SlideModel>()

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

        observerList()

        return binding.root
    }

    private fun observerList() {
        viewModel.productImageLiveData.observe(viewLifecycleOwner) {
            for (item in it) {
                productImageList.add(SlideModel(item?.productImage))
            }
            binding.productImageSlider.setImageList(productImageList, ScaleTypes.FIT)
        }
    }
}





class ProductDetailsViewModelFactory(private val repository: ProductDetailsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ProductDetailsViewModel(repository) as T
}