package com.shourov.furnitureshop.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.database.tables.FavouriteTable
import com.shourov.furnitureshop.model.ProductImageModel
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.repository.ProductDetailsRepository

class ProductDetailsViewModel(private val repository: ProductDetailsRepository) : ViewModel() {
    private val _productImagesLiveData = MutableLiveData<List<ProductImageModel?>>()
    val productImageLiveData: LiveData<List<ProductImageModel?>>
        get() = _productImagesLiveData

    fun getProductImages(productId: String?) =
        _productImagesLiveData.postValue(repository.getProductImages(productId))

    private val _productDetailsLiveData = MutableLiveData<ProductModel?>()
    val productDetailsLiveData: LiveData<ProductModel?>
        get() = _productDetailsLiveData

    fun getProductDetails(productId: String?) =
        _productDetailsLiveData.postValue(repository.getProductDetails(productId))

    private val _totalAmountLiveData = MutableLiveData<Double?>()
    val totalAmountLiveData: LiveData<Double?>
        get() = _totalAmountLiveData

    fun getTotalAmount(productPrice: Double?, productQuantity: Int?) =
        _totalAmountLiveData.postValue(repository.getTotalAmount(productPrice, productQuantity))

    fun checkIfProductIsInFavourite(userId: Int?, productId: String?): LiveData<Int> = repository.checkIfProductIsInFavourite(userId, productId)

    suspend fun insertFavourite(favourite: FavouriteTable?): Long = repository.insertFavourite(favourite)

    suspend fun deleteFavouriteById(userId: Int?, productId: String?) = repository.deleteFavouriteById(userId, productId)
}