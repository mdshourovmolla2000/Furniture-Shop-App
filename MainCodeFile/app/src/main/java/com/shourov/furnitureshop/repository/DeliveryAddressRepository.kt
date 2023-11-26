package com.shourov.furnitureshop.repository

import androidx.lifecycle.LiveData
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.AddressTable

class DeliveryAddressRepository(private val dao: AppDao) {
    fun getAddressData(userId: Int?): LiveData<List<AddressTable>> = dao.getAddressData(userId)

    suspend fun insertAddress(address: AddressTable?, callback: (message: String?) -> Unit) {
        val result = dao.insertAddress(address!!)

        if (result > 0) {
            callback("Address added")
        } else {
            callback("Something wrong")
        }
    }
}