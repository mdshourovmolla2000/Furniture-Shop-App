package com.shourov.furnitureshop.repository

import androidx.lifecycle.LiveData
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.FavouriteTable
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.model.HomeCategoryModel
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.model.SpecialOfferModel
import com.shourov.furnitureshop.utils.DemoData

class HomeRepository(private val dao: AppDao) {
    fun getUserInfo(id: Int?): LiveData<UserTable?> = dao.getUserInfo(id)

    fun getSpecialOfferData(): List<SpecialOfferModel> = DemoData().specialOfferData()

    fun getCategory(): List<HomeCategoryModel> = DemoData().homeCategoryData()

    fun getPopularProduct(categoryName: String): List<ProductModel> {
        return if (categoryName == "All") {
            DemoData().productData()
        } else {
            DemoData().productData().filter { it.itemCategory == categoryName }
        }
    }

    fun checkIfProductIsInFavourite(userId: Int?, productId: String?): LiveData<Int> = dao.checkIfProductIsInFavourite(userId, productId)

    suspend fun insertFavourite(favourite: FavouriteTable?) = dao.insertFavourite(favourite)

    suspend fun deleteFavouriteById(userId: Int?, productId: String?) = dao.deleteFavouriteById(userId, productId)
}