package com.shourov.furnitureshop.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.adapter.CategoryListAdapter
import com.shourov.furnitureshop.databinding.FragmentCategoryBinding
import com.shourov.furnitureshop.interfaces.CategoryItemClickListener
import com.shourov.furnitureshop.model.CategoryModel
import com.shourov.furnitureshop.repository.CategoryRepository
import com.shourov.furnitureshop.view_model.CategoryViewModel

class CategoryFragment : Fragment(), CategoryItemClickListener {

    private lateinit var binding: FragmentCategoryBinding

    private lateinit var viewModel: CategoryViewModel

    private val categoryList = ArrayList<CategoryModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, CategoryViewModelFactory(CategoryRepository()))[CategoryViewModel::class.java]

        viewModel.getCategory()

        observerList()

        binding.apply {
            categoryRecyclerview.adapter = CategoryListAdapter(categoryList, this@CategoryFragment)
            backIcon.setOnClickListener { findNavController().popBackStack() }
        }

        return binding.root
    }

    private fun observerList() {
        viewModel.categoryLiveData.observe(viewLifecycleOwner) {
            categoryList.clear()
            categoryList.addAll(it)
            binding.categoryRecyclerview.adapter?.notifyDataSetChanged()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onLoadCategoryItem(currentItem: CategoryModel, categoryProductCountTextview: TextView?) {
        categoryProductCountTextview?.text = "${viewModel.categoryProductCount(currentItem.categoryName)} products"
    }

    override fun onCategoryItemClick(categoryName: String) {
        val bundle = bundleOf(
            "CATEGORY_NAME" to categoryName
        )
        findNavController().navigate(R.id.action_categoryFragment_to_categoryProductFragment, bundle)
    }
}



class CategoryViewModelFactory(private val repository: CategoryRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CategoryViewModel(repository) as T
}