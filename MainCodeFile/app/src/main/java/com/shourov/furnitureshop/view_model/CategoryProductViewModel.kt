package com.shourov.furnitureshop.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.database.tables.ShoppingTable
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.repository.CategoryProductRepository

class CategoryProductViewModel(private val repository: CategoryProductRepository): ViewModel() {
    private val _categoryProductLiveData = MutableLiveData<List<ProductModel>>()
    val categoryProductLiveData: LiveData<List<ProductModel>> get() = _categoryProductLiveData
    fun getCategoryProduct(categoryName: String) = _categoryProductLiveData.postValue(repository.getCategoryProduct(categoryName))

    fun checkIfProductIsInShopping(userId: Int?, productId: String?): LiveData<Int> = repository.checkIfProductIsInShopping(userId, productId)

    suspend fun insertShopping(shopping: ShoppingTable) = repository.insertShopping(shopping)

    suspend fun deleteShoppingById(userId: Int?, productId: String?) = repository.deleteShoppingById(userId, productId)
}