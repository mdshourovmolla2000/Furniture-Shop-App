package com.shourov.furnitureshop.view

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.adapter.CategoryProductListAdapter
import com.shourov.furnitureshop.application.BaseApplication.Companion.database
import com.shourov.furnitureshop.database.tables.ShoppingTable
import com.shourov.furnitureshop.databinding.FragmentCategoryProductBinding
import com.shourov.furnitureshop.interfaces.CategoryProductItemClickListener
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.repository.CategoryProductRepository
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.viewModel.CategoryProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        categoryName = arguments?.getString("CATEGORY_NAME", "").toString()

        repository = CategoryProductRepository(database.appDao())
        viewModel = ViewModelProvider(this, CategoryProductViewModelFactory(repository))[CategoryProductViewModel::class.java]

        viewModel.getCategoryProduct(categoryName)

        observerList()

        binding.apply {
            productRecyclerview.adapter = CategoryProductListAdapter(productList, this@CategoryProductFragment)
            backIcon.setOnClickListener { findNavController().popBackStack() }
            titleTextview.text = categoryName
        }

        return binding.root
    }

    private fun observerList() {
        viewModel.categoryProductLiveData.observe(viewLifecycleOwner) {
            productList.clear()
            if (it.isEmpty()) {
                binding.apply {
                    productRecyclerview.visibility = View.GONE
                    noItemLayout.visibility = View.VISIBLE
                }
            } else {
                productList.addAll(it)
                binding.apply {
                    noItemLayout.visibility = View.GONE
                    productRecyclerview.visibility = View.VISIBLE
                }
            }

            binding.productRecyclerview.adapter?.notifyDataSetChanged()
        }
    }

    override fun onLoadProductItem(currentItem: ProductModel, cartIconCardView: CardView, cartIconImageview: ImageView) {
        viewModel.checkIfProductIsInShopping(SharedPref.read("CURRENT_USER_ID", "0")?.toInt(), currentItem.itemId).observe(viewLifecycleOwner) {
            if (it > 0) {
                cartIconCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.themeColor))
                cartIconImageview.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN)
            } else {
                cartIconCardView.setCardBackgroundColor(Color.parseColor("#F9F9F9"))
                cartIconImageview.setColorFilter(Color.parseColor("#828A89"), PorterDuff.Mode.SRC_IN)
            }
        }
    }

    override fun onProductItemClick(currentItem: ProductModel, cartIconCardView: CardView, clickOn: String?) {
        when(clickOn) {
            "MAIN_ITEM" -> {
                val bundle = bundleOf(
                    "PRODUCT_ID" to currentItem.itemId
                )
                findNavController().navigate(R.id.action_categoryProductFragment_to_productDetailsFragment, bundle)
            }
            "CART_ICON" -> {
                val cartIconCardViewBgColor = cartIconCardView.cardBackgroundColor.defaultColor
                val hexColor = String.format("#%06X", 0xFFFFFF and cartIconCardViewBgColor)
                if (hexColor == "#0C8A7B") {
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.deleteShoppingById(SharedPref.read("CURRENT_USER_ID", "0")?.toInt(), currentItem.itemId)
                    }
                } else {
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.insertShopping(ShoppingTable(0, currentItem.itemId, currentItem.itemImage, currentItem.itemName, currentItem.itemCompanyName, currentItem.itemPrice, SharedPref.read("CURRENT_USER_ID", "0")?.toInt(), 1, false))
                    }
                }
            }
        }
    }
}



class CategoryProductViewModelFactory(private val repository: CategoryProductRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CategoryProductViewModel(repository) as T
}