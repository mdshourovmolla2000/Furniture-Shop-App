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
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.adapter.PopularProductListAdapter
import com.shourov.furnitureshop.application.BaseApplication.Companion.database
import com.shourov.furnitureshop.database.tables.CartTable
import com.shourov.furnitureshop.databinding.FragmentSearchBinding
import com.shourov.furnitureshop.interfaces.PopularProductItemClickListener
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.repository.SearchRepository
import com.shourov.furnitureshop.utils.NetworkManager
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.utils.showErrorToast
import com.shourov.furnitureshop.viewModel.SearchViewModel

class SearchFragment : Fragment(), PopularProductItemClickListener {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var repository: SearchRepository
    private lateinit var viewModel: SearchViewModel

    private var productList = ArrayList<ProductModel>()
    private var searchText = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        repository = SearchRepository(database.appDao())
        viewModel = ViewModelProvider(this, SearchViewModelFactory(repository))[SearchViewModel::class.java]

        binding.apply {
            productListRecyclerview.adapter = PopularProductListAdapter(productList, this@SearchFragment)

            clearIcon.setOnClickListener { searchEdittext.text.clear() }

            searchEdittext.doOnTextChanged { text, _, _, _ ->
                searchText = text.toString()
                if (searchText.isNotEmpty()){
                    progressBar.visibility = View.VISIBLE
                    search(searchText)
                } else {
                    productList.clear()
                    productListRecyclerview.adapter?.notifyDataSetChanged()
                    noItemLayout.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
            }
        }

        return binding.root
    }

    private fun search(title: String) {
        viewModel.search(title) { data, message ->
            binding.apply {
                when(message) {
                    "Something wrong" -> {
                        requireContext().showErrorToast(message)
                    }
                    "Network error" -> {
                        requireContext().showErrorToast(message)
                    }
                    "Successful" -> {
                        productList.clear()
                        if (data.isNullOrEmpty()) {
                            productListRecyclerview.visibility = View.GONE
                            noItemLayout.visibility = View.VISIBLE
                        } else {
                            productList.addAll(ArrayList(data).asReversed())

                            noItemLayout.visibility = View.GONE
                            productListRecyclerview.visibility = View.VISIBLE
                        }
                        productListRecyclerview.adapter?.notifyDataSetChanged()
                    }
                }

                progressBar.visibility = View.GONE
            }
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

    override fun onProductItemClick(currentItem: ProductModel, cartIconCardView: CardView, clickedOn: String?) {
        when(clickedOn) {
            "MAIN_ITEM" -> {
                if (NetworkManager.isInternetAvailable(requireContext())) {
                    val bundle = bundleOf(
                        "PRODUCT_ID" to currentItem.itemId
                    )
                    findNavController().navigate(R.id.action_searchFragment_to_productDetailsFragment, bundle)
                } else {
                    requireContext().showErrorToast("No internet available")
                }
            }
            "CART_ICON" -> {
                val cartIconCardViewBgColor = cartIconCardView.cardBackgroundColor.defaultColor
                val hexColor = String.format("#%06X", 0xFFFFFF and cartIconCardViewBgColor)
                if (hexColor == String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(requireContext(), R.color.themeColor))) {
                    viewModel.deleteShoppingById(SharedPref.read("CURRENT_USER_ID", "0")?.toInt(), currentItem.itemId)
                } else {
                    viewModel.insertShopping(CartTable(0, currentItem.itemId, currentItem.itemImage, currentItem.itemName, currentItem.itemCompanyName, currentItem.itemPrice, SharedPref.read("CURRENT_USER_ID", "0")?.toInt(), 1, false))
                }
            }
        }
    }
}





class SearchViewModelFactory(private val repository: SearchRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = SearchViewModel(repository) as T
}