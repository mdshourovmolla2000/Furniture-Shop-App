package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.database.tables.ShoppingTable
import com.shourov.furnitureshop.repository.ShoppingRepository

class ShoppingViewModel(private val repository: ShoppingRepository): ViewModel() {
    fun getShoppingData(userId: Int?): LiveData<List<ShoppingTable>> = repository.getShoppingData(userId)

    private val _subTotalAmountLiveData = MutableLiveData<Double>()
    val subTotalAmountLiveData: LiveData<Double> get() = _subTotalAmountLiveData
    fun getSubTotalAmount(itemList: ArrayList<ShoppingTable>) = _subTotalAmountLiveData.postValue(itemList.sumOf { ((it.itemPrice
        ?: 0.0) * (it.itemQuantity ?: 0)) })

    suspend fun updateShopping(shopping: ShoppingTable) = repository.updateShopping(shopping)

    suspend fun deleteShopping(shoppingList: List<ShoppingTable>) = repository.deleteShopping(shoppingList)

    suspend fun clearShoppingSelection() = repository.clearShoppingSelection()

    suspend fun shoppingItemCount(userId: Int?): Int = repository.shoppingItemCount(userId)
}