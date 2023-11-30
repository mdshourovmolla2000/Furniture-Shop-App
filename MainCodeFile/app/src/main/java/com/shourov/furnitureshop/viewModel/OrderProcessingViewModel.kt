package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.database.tables.OrderTable
import com.shourov.furnitureshop.repository.OrderProcessingRepository

class OrderProcessingViewModel(private val repository: OrderProcessingRepository): ViewModel() {
    fun getOrderData(userId: Int?): LiveData<List<OrderTable>> = repository.getOrderData(userId)
}