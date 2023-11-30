package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shourov.furnitureshop.database.tables.OrderTable
import com.shourov.furnitureshop.repository.CheckoutRepository
import kotlinx.coroutines.launch

class CheckoutViewModel(private val repository: CheckoutRepository): ViewModel() {
    fun placeOrder(order: OrderTable?, callback: (message: String?) -> Unit) = viewModelScope.launch { repository.placeOrder(order, callback) }
}