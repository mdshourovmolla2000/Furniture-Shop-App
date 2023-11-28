package com.shourov.furnitureshop.repository

import androidx.lifecycle.LiveData
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.ShoppingTable
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.utils.DemoData
import java.io.IOException
import java.net.SocketException

class CategoryProductRepository(private val dao: AppDao) {
    fun getCategoryProduct(categoryName: String, callback: (data: List<ProductModel>?, message: String?) -> Unit) {
        try {
            val response = DemoData().productData().filter { it.itemCategory == categoryName }

            callback(response, "Successful")
        }
        catch (e: IOException) { callback(null, "Network error") }
        catch (e: SocketException) { callback(null, "Network error") }
        catch (e: Exception) { callback(null, "Something wrong") }
    }

    fun checkIfProductIsInShopping(userId: Int?, productId: String?): LiveData<Int> = dao.checkIfProductIsInShopping(userId, productId)

    suspend fun insertShopping(shopping: ShoppingTable) = dao.insertShopping(shopping)

    suspend fun deleteShoppingById(userId: Int?, productId: String?) = dao.deleteShoppingById(userId, productId)
}