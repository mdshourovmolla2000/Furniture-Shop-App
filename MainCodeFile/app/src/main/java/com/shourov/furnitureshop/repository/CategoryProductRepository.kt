package com.shourov.furnitureshop.repository

import androidx.lifecycle.LiveData
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.ShoppingTable
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.utils.DemoData

class CategoryProductRepository(private val dao: AppDao) {
    fun getCategoryProduct(categoryName: String): List<ProductModel> = DemoData().productData().filter { it.itemCategory == categoryName }

    fun checkIfProductIsInShopping(userId: Int?, productId: String?): LiveData<Int> = dao.checkIfProductIsInShopping(userId, productId)

    suspend fun insertShopping(shopping: ShoppingTable) = dao.insertShopping(shopping)

    suspend fun deleteShoppingById(userId: Int?, productId: String?) = dao.deleteShoppingById(userId, productId)
}