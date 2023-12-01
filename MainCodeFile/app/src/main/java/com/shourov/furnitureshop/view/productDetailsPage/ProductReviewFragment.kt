package com.shourov.furnitureshop.view.productDetailsPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.adapter.ProductReviewImageListAdapter
import com.shourov.furnitureshop.adapter.ProductReviewListAdapter
import com.shourov.furnitureshop.databinding.FragmentProductReviewBinding
import com.shourov.furnitureshop.interfaces.ProductReviewImageItemClickListener
import com.shourov.furnitureshop.interfaces.ProductReviewItemClickListener
import com.shourov.furnitureshop.model.ProductReviewModel
import com.shourov.furnitureshop.repository.ProductReviewRepository
import com.shourov.furnitureshop.utils.NetworkManager
import com.shourov.furnitureshop.utils.showErrorToast
import com.shourov.furnitureshop.viewModel.ProductReviewViewModel

class ProductReviewFragment : Fragment(), ProductReviewItemClickListener, ProductReviewImageItemClickListener {

    private lateinit var binding: FragmentProductReviewBinding

    private lateinit var repository: ProductReviewRepository
    private lateinit var viewModel: ProductReviewViewModel

    private var productId = ""
    private val reviewList = ArrayList<ProductReviewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductReviewBinding.inflate(inflater, container, false)

        binding.progressBar.visibility = View.VISIBLE

        productId = arguments?.getString("DATA", "").toString()

        repository = ProductReviewRepository()
        viewModel = ViewModelProvider(this, ProductReviewViewModelFactory(repository))[ProductReviewViewModel::class.java]

        getProductReview(productId)

        binding.apply {
            backIcon.setOnClickListener { findNavController().popBackStack() }
            reviewItemRecyclerview.adapter = ProductReviewListAdapter(reviewList, this@ProductReviewFragment)
        }

        return binding.root
    }

    private fun getProductReview(productId: String) {
        viewModel.getProductReview(productId) { data, message ->
            binding.apply {
                when(message) {
                    "Something wrong" -> requireContext().showErrorToast(message)
                    "Network error" -> requireContext().showErrorToast(message)
                    "Successful" -> {
                        reviewList.clear()

                        if (data.isNullOrEmpty()) {
                            reviewItemRecyclerview.visibility = View.GONE
                            noItemLayout.visibility = View.VISIBLE
                        } else {
                            reviewList.addAll(data.asReversed())

                            noItemLayout.visibility = View.GONE
                            reviewItemRecyclerview.visibility = View.VISIBLE
                        }

                        reviewItemRecyclerview.adapter?.notifyDataSetChanged()
                    }
                }

                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onLoadReviewItem(currentItem: ProductReviewModel?, reviewImageRecyclerview: RecyclerView?) {
        val reviewImageList = currentItem?.reviewImages
        reviewImageRecyclerview?.adapter = ProductReviewImageListAdapter(ArrayList(reviewImageList), this)
    }

    override fun onClickProductReviewImageItem(currentItem: Int?) {
        if (NetworkManager.isInternetAvailable(requireContext())) {
            val bundle = bundleOf(
                "DATA" to currentItem
            )
            findNavController().navigate(R.id.action_productReviewFragment_to_fullImageFragment, bundle)
        } else {
            requireContext().showErrorToast("No internet available")
        }
    }
}





class ProductReviewViewModelFactory(private val repository: ProductReviewRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ProductReviewViewModel(repository) as T
}