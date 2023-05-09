package com.shourov.furnitureshop.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.model.ShoppingModel
import com.shourov.furnitureshop.repository.ShoppingRepository

class ShoppingViewModel(private val repository: ShoppingRepository): ViewModel() {
    private val _shoppingLiveData = MutableLiveData<ArrayList<ShoppingModel>>()
    val shoppingLiveData: LiveData<ArrayList<ShoppingModel>>
        get() = _shoppingLiveData

    fun getShoppingData() = _shoppingLiveData.postValue(repository.getShoppingData())

    private val _subTotalAmountLiveData = MutableLiveData<Double>()
    val subTotalAmountLiveData: LiveData<Double>
        get() = _subTotalAmountLiveData

    fun getSubTotalAmount(itemList: ArrayList<ShoppingModel>) = _subTotalAmountLiveData.postValue(itemList.sumOf { ((it.itemPrice ?: 0.0) * (it.itemQuantity ?: 0)) })
}