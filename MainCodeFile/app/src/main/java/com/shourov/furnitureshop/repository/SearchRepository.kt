package com.shourov.furnitureshop.repository

import androidx.lifecycle.LiveData
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.CartTable
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.utils.DemoData
import java.io.IOException
import java.net.SocketException

class SearchRepository(private val dao: AppDao) {
    fun search(title: String?, callback: (data: List<ProductModel>?, message: String?) -> Unit) {
        try {
            val response = DemoData().productData().filter { it.itemName?.lowercase()?.contains(title?.lowercase().toString()) == true }

            callback(response, "Successful")
        }
        catch (e: IOException) { callback(null, "Network error") }
        catch (e: SocketException) { callback(null, "Network error") }
        catch (e: Exception) { callback(null, "Something wrong") }
    }

    fun checkIfProductIsInShopping(userId: Int?, productId: String?): LiveData<Int> = dao.checkIfProductIsInCart(userId, productId)

    suspend fun insertShopping(shopping: CartTable) = dao.insertCart(shopping)

    suspend fun deleteShoppingById(userId: Int?, productId: String?) = dao.deleteCartById(userId, productId)
}