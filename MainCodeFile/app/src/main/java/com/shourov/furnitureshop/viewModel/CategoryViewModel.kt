package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.model.CategoryModel
import com.shourov.furnitureshop.repository.CategoryRepository

class CategoryViewModel(private val repository: CategoryRepository): ViewModel() {
    private val _categoryLiveData = MutableLiveData<List<CategoryModel>>()
    val categoryLiveData: LiveData<List<CategoryModel>> get() = _categoryLiveData
    fun getCategory() = _categoryLiveData.postValue(repository.getCategory())

    fun categoryProductCount(categoryName: String?): Int = repository.categoryProductCount(categoryName)
}