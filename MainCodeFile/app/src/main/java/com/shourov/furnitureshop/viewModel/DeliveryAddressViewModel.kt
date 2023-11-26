package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shourov.furnitureshop.database.tables.AddressTable
import com.shourov.furnitureshop.repository.DeliveryAddressRepository
import kotlinx.coroutines.launch

class DeliveryAddressViewModel(private val repository: DeliveryAddressRepository): ViewModel() {
    fun getAddressData(userId: Int?): LiveData<List<AddressTable>> = repository.getAddressData(userId)

    fun insertAddress(address: AddressTable?, callback: (message: String?) -> Unit) = viewModelScope.launch { repository.insertAddress(address, callback) }
}