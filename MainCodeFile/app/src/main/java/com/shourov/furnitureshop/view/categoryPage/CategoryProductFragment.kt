package com.shourov.furnitureshop.view.categoryPage

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.adapter.CategoryProductListAdapter
import com.shourov.furnitureshop.application.BaseApplication.Companion.database
import com.shourov.furnitureshop.database.tables.CartTable
import com.shourov.furnitureshop.databinding.FragmentCategoryProductBinding
import com.shourov.furnitureshop.interfaces.CategoryProductItemClickListener
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.repository.CategoryProductRepository
import com.shourov.furnitureshop.utils.NetworkManager
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.utils.showErrorToast
import com.shourov.furnitureshop.viewModel.CategoryProductViewModel

class CategoryProductFragment : Fragment(), CategoryProductItemClickListener {

    private lateinit var binding: FragmentCategoryProductBinding

    private lateinit var repository: CategoryProductRepository
    private lateinit var viewModel: CategoryProductViewModel

    private var categoryName = ""
    private val productList = ArrayList<ProductModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryProductBinding.inflate(inflater, container, false)

        binding.progressBar.visibility = View.VISIBLE

        categoryName = arguments?.getString("CATEGORY_NAME", "").toString()

        repository = CategoryProductRepository(database.appDao())
        viewModel = ViewModelProvider(this, CategoryProductViewModelFactory(repository))[CategoryProductViewModel::class.java]

        getCategoryProduct()

        binding.apply {
            backIcon.setOnClickListener { findNavController().popBackStack() }
            titleTextview.text = categoryName
            productRecyclerview.adapter = CategoryProductListAdapter(productList, this@CategoryProductFragment)
        }

        return binding.root
    }

    private fun getCategoryProduct() {
        viewModel.getCategoryProduct(categoryName) { data, message ->
            binding.apply {
                when(message) {
                    "Something wrong" -> requireContext().showErrorToast(message)
                    "Network error" -> requireContext().showErrorToast(message)
                    "Successful" -> {
                        productList.clear()

                        if (data.isNullOrEmpty()) {
                            productRecyclerview.visibility = View.GONE
                            noItemLayout.visibility = View.VISIBLE
                        } else {
                            productList.addAll(data.asReversed())

                            noItemLayout.visibility = View.GONE
                            productRecyclerview.visibility = View.VISIBLE
                        }

                        productRecyclerview.adapter?.notifyDataSetChanged()
                    }
                }

                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onLoadProductItem(currentItem: ProductModel, cartIconCardView: CardView, cartIconImageview: ImageView) {
        viewModel.checkIfProductIsInShopping(SharedPref.read("CURRENT_USER_ID", "0")?.toInt(), currentItem.id).observe(viewLifecycleOwner) {
            if (it > 0) {
                cartIconCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.themeColor))
                cartIconImageview.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN)
            } else {
                cartIconCardView.setCardBackgroundColor(Color.parseColor("#F9F9F9"))
                cartIconImageview.setColorFilter(Color.parseColor("#828A89"), PorterDuff.Mode.SRC_IN)
            }
        }
    }

    override fun onProductItemClick(currentItem: ProductModel, cartIconCardView: CardView, clickedOn: String?) {
        when(clickedOn) {
            "MAIN_ITEM" -> {
                if (NetworkManager.isInternetAvailable(requireContext())) {
                    val bundle = bundleOf(
                        "PRODUCT_ID" to currentItem.id
                    )
                    findNavController().navigate(R.id.action_categoryProductFragment_to_productDetailsFragment, bundle)
                } else {
                    requireContext().showErrorToast("No internet available")
                }
            }
            "CART_ICON" -> {
                val cartIconCardViewBgColor = cartIconCardView.cardBackgroundColor.defaultColor
                val hexColor = String.format("#%06X", 0xFFFFFF and cartIconCardViewBgColor)
                if (hexColor == String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(requireContext(), R.color.themeColor))) {
                    viewModel.deleteShoppingById(SharedPref.read("CURRENT_USER_ID", "0")?.toInt(), currentItem.id)
                } else {
                    viewModel.insertShopping(CartTable(0, currentItem.id, currentItem.itemImage, currentItem.itemName, currentItem.itemCompanyName, currentItem.itemPrice, SharedPref.read("CURRENT_USER_ID", "0")?.toInt(), 1, false))
                }
            }
        }
    }
}



class CategoryProductViewModelFactory(private val repository: CategoryProductRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CategoryProductViewModel(repository) as T
}