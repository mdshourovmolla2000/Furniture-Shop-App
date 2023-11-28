package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shourov.furnitureshop.model.CategoryModel
import com.shourov.furnitureshop.repository.CategoryRepository
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoryRepository): ViewModel() {
    fun getCategory(callback: (data: List<CategoryModel>?, message: String?) -> Unit) = viewModelScope.launch { repository.getCategory(callback) }

    fun categoryProductCount(categoryName: String?): Int = repository.categoryProductCount(categoryName)
}