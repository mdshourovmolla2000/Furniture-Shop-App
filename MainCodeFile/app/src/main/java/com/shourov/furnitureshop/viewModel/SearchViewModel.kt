package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shourov.furnitureshop.database.tables.CartTable
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.repository.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: SearchRepository): ViewModel() {
    fun search(title: String, callback: (data: List<ProductModel>?, message: String?) -> Unit) = viewModelScope.launch { repository.search(title, callback) }

    fun checkIfProductIsInShopping(userId: Int?, productId: String?): LiveData<Int> = repository.checkIfProductIsInShopping(userId, productId)

    fun insertShopping(shopping: CartTable) = viewModelScope.launch { repository.insertShopping(shopping) }

    fun deleteShoppingById(userId: Int?, productId: String?) = viewModelScope.launch { repository.deleteShoppingById(userId, productId) }
}