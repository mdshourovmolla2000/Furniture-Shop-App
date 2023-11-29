package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shourov.furnitureshop.database.tables.FavouriteTable
import com.shourov.furnitureshop.database.tables.CartTable
import com.shourov.furnitureshop.model.ProductImageModel
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.repository.ProductDetailsRepository
import kotlinx.coroutines.launch

class ProductDetailsViewModel(private val repository: ProductDetailsRepository) : ViewModel() {
    private val _productImagesLiveData = MutableLiveData<List<ProductImageModel?>>()
    val productImageLiveData: LiveData<List<ProductImageModel?>> get() = _productImagesLiveData
    fun getProductImages(productId: String?) = _productImagesLiveData.postValue(repository.getProductImages(productId))

    private val _productDetailsLiveData = MutableLiveData<ProductModel?>()
    val productDetailsLiveData: LiveData<ProductModel?> get() = _productDetailsLiveData
    fun getProductDetails(productId: String?) = _productDetailsLiveData.postValue(repository.getProductDetails(productId))

    private val _totalAmountLiveData = MutableLiveData<Double?>()
    val totalAmountLiveData: LiveData<Double?> get() = _totalAmountLiveData
    fun getTotalAmount(productPrice: Double?, productQuantity: Int?) = _totalAmountLiveData.postValue(repository.getTotalAmount(productPrice, productQuantity))

    fun checkIfProductIsInFavourite(userId: Int?, productId: String?): LiveData<Int> = repository.checkIfProductIsInFavourite(userId, productId)

    fun insertFavourite(favourite: FavouriteTable) = viewModelScope.launch { repository.insertFavourite(favourite) }

    fun deleteFavouriteById(userId: Int?, productId: String?) = viewModelScope.launch { repository.deleteFavouriteById(userId, productId) }

    suspend fun insertShopping(shopping: CartTable) = repository.insertShopping(shopping)

    fun checkIfProductIsInShopping(userId: Int?, productId: String?): LiveData<Int> = repository.checkIfProductIsInShopping(userId, productId)

    suspend fun deleteShoppingById(userId: Int?, productId: String?) = repository.deleteShoppingById(userId, productId)
}