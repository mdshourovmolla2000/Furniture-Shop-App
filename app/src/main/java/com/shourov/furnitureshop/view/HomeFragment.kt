package com.shourov.furnitureshop.view

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.adapter.HomeCategoryListAdapter
import com.shourov.furnitureshop.adapter.PopularProductListAdapter
import com.shourov.furnitureshop.adapter.SpecialOffersListAdapter
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.AppDatabase
import com.shourov.furnitureshop.databinding.DialogExitBinding
import com.shourov.furnitureshop.databinding.FragmentHomeBinding
import com.shourov.furnitureshop.interfaces.HomeCategoryItemClickListener
import com.shourov.furnitureshop.interfaces.PopularProductItemClickListener
import com.shourov.furnitureshop.model.HomeCategoryModel
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.model.SpecialOfferModel
import com.shourov.furnitureshop.repository.HomeRepository
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.utils.loadImage
import com.shourov.furnitureshop.view_model.HomeViewModel

class HomeFragment : Fragment(), HomeCategoryItemClickListener, PopularProductItemClickListener {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var dao: AppDao
    private lateinit var repository: HomeRepository
    private lateinit var viewModel: HomeViewModel

    private val specialOfferItemsList = ArrayList<SpecialOfferModel>()

    private val categoryList = ArrayList<HomeCategoryModel>()
    private var currentCategoryPosition = 0
    private var currentCategory = "All"

    private val popularProductList = ArrayList<ProductModel>()
    private var scrollPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(requireContext())
                val dialogBinding = DialogExitBinding.inflate(layoutInflater)

                builder.setView(dialogBinding.root)
                builder.setCancelable(true)

                val alertDialog = builder.create()

                //make transparent to default dialog
                alertDialog.window?.setBackgroundDrawable(ColorDrawable(0))

                dialogBinding.noButton.setOnClickListener { alertDialog.dismiss() }

                dialogBinding.yesButton.setOnClickListener {
                    alertDialog.dismiss()
                    requireActivity().finish()
                }

                alertDialog.show()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        scrollPosition = binding.mainScrollView.scrollY
        outState.putInt("SCROLL_POSITION", scrollPosition)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            scrollPosition = savedInstanceState.getInt("SCROLL_POSITION")
            binding.mainScrollView.post { binding.mainScrollView.scrollTo(0, scrollPosition) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        SharedPref.init(requireContext())

        dao = AppDatabase.getDatabase(requireContext()).appDao()
        repository = HomeRepository(dao)
        viewModel = ViewModelProvider(this, HomeViewModelFactory(repository))[HomeViewModel::class.java]

        viewModel.getSpecialOfferData()
        viewModel.getCategory()
        viewModel.getPopularProduct(currentCategory)

        observerList()

        binding.specialOfferRecyclerview.apply {
            adapter = SpecialOffersListAdapter(specialOfferItemsList)
        }

        binding.categoryRecyclerview.apply {
            adapter = HomeCategoryListAdapter(categoryList, currentCategoryPosition, this@HomeFragment)
        }

        binding.popularItemsRecyclerview.apply {
            adapter = PopularProductListAdapter(popularProductList, this@HomeFragment)
        }

        binding.bottomNavigationFavouriteMenu.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_favouriteFragment) }
        binding.bottomNavigationShoppingMenu.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_shoppingFragment) }

        return binding.root
    }

    private fun observerList() {
        viewModel.getUserInfo(SharedPref.read("CURRENT_USER_ID", "0")?.toInt()).observe(viewLifecycleOwner) {
            it?.let {
                binding.profilePicImageview.loadImage(it.profile_pic.toUri())
                binding.userNameTextview.text = it.name
            }
        }

        viewModel.specialOfferLiveData.observe(viewLifecycleOwner) {
            specialOfferItemsList.clear()
            specialOfferItemsList.addAll(it)
            binding.specialOfferRecyclerview.adapter?.notifyDataSetChanged()
        }

        viewModel.categoryLiveData.observe(viewLifecycleOwner) {
            categoryList.clear()
            categoryList.addAll(ArrayList(it))
            binding.categoryRecyclerview.adapter?.notifyDataSetChanged()
        }

        viewModel.popularProductLiveData.observe(viewLifecycleOwner) {
            popularProductList.clear()
            if (it.isEmpty()) {
                binding.popularItemsRecyclerview.visibility = View.GONE
                binding.noPopularItemLayout.visibility = View.VISIBLE
            } else {
                popularProductList.addAll(it)
                binding.noPopularItemLayout.visibility = View.GONE
                binding.popularItemsRecyclerview.visibility = View.VISIBLE
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

    override fun onProductItemClick(currentItem: ProductModel) {

    }
}




class HomeViewModelFactory(private val repository: HomeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel(repository) as T
}