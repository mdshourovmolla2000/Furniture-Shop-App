package com.shourov.furnitureshop.repository

import androidx.lifecycle.LiveData
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.ShoppingTable
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

    fun checkIfProductIsInShopping(userId: Int?, productId: String?): LiveData<Int> = dao.checkIfProductIsInShopping(userId, productId)

    suspend fun insertShopping(shopping: ShoppingTable?) = dao.insertShopping(shopping)

    suspend fun deleteShoppingById(userId: Int?, productId: String?) = dao.deleteShoppingById(userId, productId)
}