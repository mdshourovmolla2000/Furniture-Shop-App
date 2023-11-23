package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.database.tables.ShoppingTable
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.model.HomeCategoryModel
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.model.SpecialOfferModel
import com.shourov.furnitureshop.repository.HomeRepository

class HomeViewModel(private val repository: HomeRepository): ViewModel() {
    fun getUserInfo(id: Int?): LiveData<UserTable?> = repository.getUserInfo(id)

    private val _specialOfferLiveData = MutableLiveData<List<SpecialOfferModel>>()
    val specialOfferLiveData: LiveData<List<SpecialOfferModel>> get() = _specialOfferLiveData
    fun getSpecialOfferData() = _specialOfferLiveData.postValue(repository.getSpecialOfferData())

    private val _categoryLiveData = MutableLiveData<List<HomeCategoryModel>>()
    val categoryLiveData: LiveData<List<HomeCategoryModel>> get() = _categoryLiveData
    fun getCategory() = _categoryLiveData.postValue(repository.getCategory())

    private val _popularProductLiveData = MutableLiveData<List<ProductModel>>()
    val popularProductLiveData: LiveData<List<ProductModel>> get() = _popularProductLiveData
    fun getPopularProduct(categoryName: String) = _popularProductLiveData.postValue(repository.getPopularProduct(categoryName))

    fun checkIfProductIsInShopping(userId: Int?, productId: String?): LiveData<Int> = repository.checkIfProductIsInShopping(userId, productId)

    suspend fun insertShopping(shopping: ShoppingTable) = repository.insertShopping(shopping)

    suspend fun deleteShoppingById(userId: Int?, productId: String?) = repository.deleteShoppingById(userId, productId)
}