package com.shourov.furnitureshop.repository

import androidx.lifecycle.LiveData
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.FavouriteTable
import com.shourov.furnitureshop.model.ProductImageModel
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.utils.DemoData

class ProductDetailsRepository(private val dao: AppDao) {
    fun getProductImages(productId: String?): List<ProductImageModel?> = DemoData().productImageData().filter { it.productId == productId }

    fun getProductDetails(productId: String?): ProductModel? = DemoData().productData().find { it.itemId == productId }

    fun getTotalAmount(productPrice: Double?, productQuantity: Int?): Double = productPrice!! * productQuantity!!

    fun checkIfProductIsInFavourite(userId: Int?, productId: String?): LiveData<Int> = dao.checkIfProductIsInFavourite(userId, productId)

    suspend fun insertFavourite(favourite: FavouriteTable?): Long = dao.insertFavourite(favourite)

    suspend fun deleteFavouriteById(userId: Int?, productId: String?) = dao.deleteFavouriteById(userId, productId)
}