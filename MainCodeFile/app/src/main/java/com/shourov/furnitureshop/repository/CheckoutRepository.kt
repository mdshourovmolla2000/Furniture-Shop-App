package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.OrderTable
import java.io.IOException
import java.net.SocketException

class CheckoutRepository(private val dao: AppDao) {
    suspend fun placeOrder(order: OrderTable?, callback: (message: String?) -> Unit) {
        try {
            val result = dao.clearCart()

            if (result > 0) {
                insertOrder(order, callback)
            } else {
                callback("Something wrong")
            }
        }
        catch (e: IOException) { callback("Network error") }
        catch (e: SocketException) { callback("Network error") }
        catch (e: Exception) { callback("Something wrong") }
    }

    private suspend fun insertOrder(order: OrderTable?, callback: (message: String?) -> Unit) {
        try {
            val result = dao.insertOrder(order!!)

            if (result > 0) {
                callback("Successful")
            } else {
                callback("Something wrong")
            }
        }
        catch (e: IOException) { callback("Network error") }
        catch (e: SocketException) { callback("Network error") }
        catch (e: Exception) { callback("Something wrong") }
    }
}