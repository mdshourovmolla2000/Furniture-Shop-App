package com.shourov.furnitureshop.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.database.tables.FavouriteTable
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.model.HomeCategoryModel
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.model.SpecialOfferModel
import com.shourov.furnitureshop.repository.HomeRepository

class HomeViewModel(private val repository: HomeRepository): ViewModel() {
    fun getUserInfo(id: Int?): LiveData<UserTable?> = repository.getUserInfo(id)

    private val _specialOfferLiveData = MutableLiveData<List<SpecialOfferModel>>()
    val specialOfferLiveData: LiveData<List<SpecialOfferModel>>
        get() = _specialOfferLiveData

    fun getSpecialOfferData() = _specialOfferLiveData.postValue(repository.getSpecialOfferData())

    private val _categoryLiveData = MutableLiveData<List<HomeCategoryModel>>()
    val categoryLiveData: LiveData<List<HomeCategoryModel>>
        get() = _categoryLiveData

    fun getCategory() = _categoryLiveData.postValue(repository.getCategory())

    private val _popularProductLiveData = MutableLiveData<List<ProductModel>>()
    val popularProductLiveData: LiveData<List<ProductModel>>
        get() = _popularProductLiveData

    fun getPopularProduct(categoryName: String) = _popularProductLiveData.postValue(repository.getPopularProduct(categoryName))

    fun checkIfProductIsInFavourite(userId: Int?, productId: String?): LiveData<Int> = repository.checkIfProductIsInFavourite(userId, productId)

    suspend fun insertFavourite(favourite: FavouriteTable?): Long = repository.insertFavourite(favourite)

    suspend fun deleteFavouriteById(userId: Int?, productId: String?) = repository.deleteFavouriteById(userId, productId)
}