package com.shourov.furnitureshop.repository

import androidx.lifecycle.LiveData
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.AddressTable
import java.io.IOException
import java.net.SocketException

class DeliveryAddressRepository(private val dao: AppDao) {
    fun getAddressData(userId: Int?): LiveData<List<AddressTable>> = dao.getAddressData(userId)

    suspend fun insertAddress(address: AddressTable?, callback: (message: String?) -> Unit) {
        try {
            val result = dao.insertAddress(address!!)

            if (result > 0) {
                callback("Address added")
            } else {
                callback("Something wrong")
            }
        } catch (e: IOException) {
            callback("Network error")
        } catch (e: SocketException) {
            callback("Network error")
        } catch (e: Exception) {
            callback("Something wrong")
        }
    }

    suspend fun updateAddress(address: AddressTable?, callback: (message: String?) -> Unit) {
        try {
            val result = dao.updateAddress(address!!)

            if (result > 0) {
                callback("Updated")
            } else {
                callback("Something wrong")
            }
        } catch (e: IOException) {
            callback("Network error")
        } catch (e: SocketException) {
            callback("Network error")
        } catch (e: Exception) {
            callback("Something wrong")
        }
    }

    suspend fun deleteAddress(address: AddressTable) = dao.deleteAddress(address)
}