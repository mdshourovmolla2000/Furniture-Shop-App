package com.shourov.furnitureshop.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.model.ProductImageModel
import com.shourov.furnitureshop.repository.ProductDetailsRepository

class ProductDetailsViewModel(private val repository: ProductDetailsRepository): ViewModel() {
    private val _productImagesLiveData = MutableLiveData<List<ProductImageModel?>>()
    val productImageLiveData: LiveData<List<ProductImageModel?>>
        get() = _productImagesLiveData

    fun getProductImages(productId: String?) = _productImagesLiveData.postValue(repository.getProductImages(productId))
}