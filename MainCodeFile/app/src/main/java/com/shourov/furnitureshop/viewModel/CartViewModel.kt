package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shourov.furnitureshop.database.tables.CartTable
import com.shourov.furnitureshop.repository.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository): ViewModel() {
    fun getCartData(userId: Int?): LiveData<List<CartTable>> = repository.getCartData(userId)

    private val _subTotalAmountLiveData = MutableLiveData<Double>()
    val subTotalAmountLiveData: LiveData<Double> get() = _subTotalAmountLiveData
    fun getSubTotalAmount(itemList: ArrayList<CartTable>) = _subTotalAmountLiveData.postValue(itemList.sumOf { ((it.itemPrice
        ?: 0.0) * (it.itemQuantity ?: 0)) })

    fun updateCart(cart: CartTable) = viewModelScope.launch { repository.updateCart(cart) }

    suspend fun deleteCart(cartList: List<CartTable>) = repository.deleteCart(cartList)

    fun clearCartSelection() = viewModelScope.launch { repository.clearCartSelection() }

    suspend fun cartItemCount(userId: Int?): Int = repository.cartItemCount(userId)
}