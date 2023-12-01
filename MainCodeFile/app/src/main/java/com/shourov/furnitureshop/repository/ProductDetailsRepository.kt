package com.shourov.furnitureshop.repository

import androidx.lifecycle.LiveData
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.CartTable
import com.shourov.furnitureshop.database.tables.FavouriteTable
import com.shourov.furnitureshop.model.ProductImageModel
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.utils.DemoData
import java.io.IOException
import java.net.SocketException

class ProductDetailsRepository(private val dao: AppDao) {
    fun getProductImages(productId: String?, callback: (data: List<ProductImageModel>?, message: String?) -> Unit) {
        try {
            val response = DemoData().productImageData().filter { it.productId == productId }

            callback(response, "Successful")
        }
        catch (e: IOException) { callback(null, "Network error") }
        catch (e: SocketException) { callback(null, "Network error") }
        catch (e: Exception) { callback(null, "Something wrong") }
    }

    fun productReviewCount(productId: String?): Int = DemoData().productReviewData().count { it.productId == productId }

    fun getProductDetails(productId: String?, callback: (data: ProductModel?, message: String?) -> Unit) {
        try {
            val response = DemoData().productData().find { it.id == productId }

            callback(response, "Successful")
        }
        catch (e: IOException) { callback(null, "Network error") }
        catch (e: SocketException) { callback(null, "Network error") }
        catch (e: Exception) { callback(null, "Something wrong") }
    }

    fun getTotalAmount(productPrice: Double?, productQuantity: Int?): Double = productPrice!! * productQuantity!!

    fun checkIfProductIsInFavourite(userId: Int?, productId: String?): LiveData<Int> = dao.checkIfProductIsInFavourite(userId, productId)

    suspend fun insertFavourite(favourite: FavouriteTable) = dao.insertFavourite(favourite)

    suspend fun deleteFavouriteById(userId: Int?, productId: String?) = dao.deleteFavouriteById(userId, productId)

    suspend fun insertShopping(shopping: CartTable) = dao.insertCart(shopping)

    fun checkIfProductIsInShopping(userId: Int?, productId: String?): LiveData<Int> = dao.checkIfProductIsInCart(userId, productId)

    suspend fun deleteShoppingById(userId: Int?, productId: String?) = dao.deleteCartById(userId, productId)
}