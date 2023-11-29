package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shourov.furnitureshop.database.tables.CartTable
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.model.HomeCategoryModel
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.model.SpecialOfferModel
import com.shourov.furnitureshop.repository.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository): ViewModel() {
    fun getUserInfo(id: Int?): LiveData<UserTable?> = repository.getUserInfo(id)

    fun getSpecialOfferData(callback: (data: List<SpecialOfferModel>?, message: String?) -> Unit) = viewModelScope.launch { repository.getSpecialOfferData(callback) }

    fun getCategoryData(callback: (data: List<HomeCategoryModel>?, message: String?) -> Unit) = viewModelScope.launch { repository.getCategoryData(callback) }

    fun getPopularProductData(categoryName: String, callback: (data: List<ProductModel>?, message: String?) -> Unit) = viewModelScope.launch { repository.getPopularProductData(categoryName, callback) }

    fun checkIfProductIsInShopping(userId: Int?, productId: String?): LiveData<Int> = repository.checkIfProductIsInShopping(userId, productId)

    fun insertShopping(shopping: CartTable) = viewModelScope.launch { repository.insertShopping(shopping) }

    fun deleteShoppingById(userId: Int?, productId: String?) = viewModelScope.launch { repository.deleteShoppingById(userId, productId) }
}