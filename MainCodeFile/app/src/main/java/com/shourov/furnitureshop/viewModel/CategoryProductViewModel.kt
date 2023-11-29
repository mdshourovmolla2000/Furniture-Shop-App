package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shourov.furnitureshop.database.tables.CartTable
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.repository.CategoryProductRepository
import kotlinx.coroutines.launch

class CategoryProductViewModel(private val repository: CategoryProductRepository): ViewModel() {
    fun getCategoryProduct(categoryName: String, callback: (data: List<ProductModel>?, message: String?) -> Unit) = viewModelScope.launch { repository.getCategoryProduct(categoryName, callback) }

    fun checkIfProductIsInShopping(userId: Int?, productId: String?): LiveData<Int> = repository.checkIfProductIsInShopping(userId, productId)

    fun insertShopping(shopping: CartTable) = viewModelScope.launch { repository.insertShopping(shopping) }

    fun deleteShoppingById(userId: Int?, productId: String?) = viewModelScope.launch { repository.deleteShoppingById(userId, productId) }
}