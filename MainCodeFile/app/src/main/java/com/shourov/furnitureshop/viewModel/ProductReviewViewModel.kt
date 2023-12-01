package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shourov.furnitureshop.model.ProductReviewModel
import com.shourov.furnitureshop.repository.ProductReviewRepository
import kotlinx.coroutines.launch

class ProductReviewViewModel(private val repository: ProductReviewRepository): ViewModel() {
    fun getProductReview(productId: String?, callback: (data: List<ProductReviewModel>?, message: String?) -> Unit) = viewModelScope.launch { repository.getProductReview(productId, callback) }
}