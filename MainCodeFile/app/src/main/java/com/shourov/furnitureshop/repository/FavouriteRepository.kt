package com.shourov.furnitureshop.repository

import androidx.lifecycle.LiveData
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.FavouriteTable
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.utils.DemoData

class FavouriteRepository(private val dao: AppDao) {
    fun getFavouriteData(userId: Int?): LiveData<List<FavouriteTable>> = dao.getFavouriteData(userId)

    fun getProductDetails(productId: String?): ProductModel? = DemoData().productData().find { it.itemId == productId }

    suspend fun deleteFavourite(favourite: FavouriteTable) = dao.deleteFavourite(favourite)
}