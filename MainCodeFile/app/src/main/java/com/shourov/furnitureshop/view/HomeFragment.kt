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
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.adapter.HomeCategoryListAdapter
import com.shourov.furnitureshop.adapter.PopularProductListAdapter
import com.shourov.furnitureshop.adapter.SpecialOffersListAdapter
import com.shourov.furnitureshop.application.BaseApplication.Companion.database
import com.shourov.furnitureshop.database.tables.ShoppingTable
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.databinding.FragmentHomeBinding
import com.shourov.furnitureshop.interfaces.HomeCategoryItemClickListener
import com.shourov.furnitureshop.interfaces.PopularProductItemClickListener
import com.shourov.furnitureshop.model.HomeCategoryModel
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.model.SpecialOfferModel
import com.shourov.furnitureshop.repository.HomeRepository
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.utils.loadImage
import com.shourov.furnitureshop.viewModel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), HomeCategoryItemClickListener, PopularProductItemClickListener {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var repository: HomeRepository
    private lateinit var viewModel: HomeViewModel

    private val specialOfferItemsList = ArrayList<SpecialOfferModel>()

    private val categoryList = ArrayList<HomeCategoryModel>()
    private var currentCategoryPosition = 0
    private var currentCategory = "All"

    private val popularProductList = ArrayList<ProductModel>()
    private var scrollPosition = 0

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        scrollPosition = binding.mainScrollView.scrollY
        outState.putInt("SCROLL_POSITION", scrollPosition)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            try {
                scrollPosition = savedInstanceState.getInt("SCROLL_POSITION")
                binding.mainScrollView.post { binding.mainScrollView.scrollTo(0, scrollPosition) }
            } catch (_: Exception) {}
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        repository = HomeRepository(database.appDao())
        viewModel = ViewModelProvider(this, HomeViewModelFactory(repository))[HomeViewModel::class.java]

        viewModel.getSpecialOfferData()
        viewModel.getCategory()
        viewModel.getPopularProduct(currentCategory)

        observerList()

        binding.apply {
            notificationIcon.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_notificationFragment) }
            specialOfferRecyclerview.adapter = SpecialOffersListAdapter(specialOfferItemsList)
            categoryRecyclerview.adapter = HomeCategoryListAdapter(categoryList, currentCategoryPosition, this@HomeFragment)
            popularItemsRecyclerview.adapter = PopularProductListAdapter(popularProductList, this@HomeFragment)
        }

        updateView()

        return binding.root
    }

    private fun observerList() {
        viewModel.getUserInfo(SharedPref.read("CURRENT_USER_ID", "0")?.toInt()).observe(viewLifecycleOwner) {
            it?.let {
                binding.apply {
                    SharedPref.write("PROFILE_RESPONSE", Gson().toJson(it))
                    updateView()
                }
            }
        }

        viewModel.specialOfferLiveData.observe(viewLifecycleOwner) {
            specialOfferItemsList.clear()
            specialOfferItemsList.addAll(it)
            binding.specialOfferRecyclerview.adapter?.notifyDataSetChanged()
        }

        viewModel.categoryLiveData.observe(viewLifecycleOwner) {
            categoryList.clear()
            categoryList.addAll(it)
            binding.categoryRecyclerview.adapter?.notifyDataSetChanged()
        }

        viewModel.popularProductLiveData.observe(viewLifecycleOwner) {
            popularProductList.clear()
            if (it.isEmpty()) {
                binding.apply {
                    popularItemsRecyclerview.visibility = View.GONE
                    noPopularItemLayout.visibility = View.VISIBLE
                }
            } else {
                popularProductList.addAll(it)
                binding.apply {
                    noPopularItemLayout.visibility = View.GONE
                    popularItemsRecyclerview.visibility = View.VISIBLE
                }
            }

            binding.popularItemsRecyclerview.adapter?.notifyDataSetChanged()
        }
    }

    override fun onCategoryItemClick(currentItem: String, currentItemPosition: Int) {
        when(currentItem) {
            "More" -> findNavController().navigate(R.id.action_homeFragment_to_categoryFragment)
            else -> {
                if (currentCategoryPosition != currentItemPosition) {
                    currentCategoryPosition = currentItemPosition
                    currentCategory = currentItem
                    viewModel.getPopularProduct(currentCategory)
                }
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
                val bundle = bundleOf(
                    "PRODUCT_ID" to currentItem.itemId
                )
                findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, bundle)
            }
            "CART_ICON" -> {
                val cartIconCardViewBgColor = cartIconCardView.cardBackgroundColor.defaultColor
                val hexColor = String.format("#%06X", 0xFFFFFF and cartIconCardViewBgColor)
                if (hexColor == String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(requireContext(), R.color.themeColor))) {
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

    private fun updateView() {
        if (SharedPref.read("PROFILE_RESPONSE", "").isNullOrEmpty()) {
        } else {
            try {
                val response = Gson().fromJson(SharedPref.read("PROFILE_RESPONSE", ""), UserTable::class.java)
                binding.apply {
                    if (response.profilePic.isNullOrEmpty()) {
                        profilePicImageview.loadImage(R.drawable.user_profile_pic_placeholder_image)
                    } else {
                        profilePicImageview.loadImage(response.profilePic?.toUri())
                    }
                    userNameTextview.text = response.name
                }
            } catch (_: Exception) { }
        }
    }
}




class HomeViewModelFactory(private val repository: HomeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel(repository) as T
}