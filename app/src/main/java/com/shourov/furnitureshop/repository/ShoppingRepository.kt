package com.shourov.furnitureshop.repository

import androidx.lifecycle.LiveData
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.FavouriteTable
import com.shourov.furnitureshop.database.tables.ShoppingTable
import com.shourov.furnitureshop.model.ShoppingModel
import com.shourov.furnitureshop.utils.DemoData

class ShoppingRepository(private val dao: AppDao) {
    fun getShoppingData(userId: Int?): LiveData<List<ShoppingTable?>?> = dao.getShoppingData(userId)

    suspend fun updateShopping(shopping: ShoppingTable?) = dao.updateShopping(shopping)

    suspend fun deleteShopping(shoppingList: List<ShoppingTable?>) = dao.deleteShopping(shoppingList)

    suspend fun clearShoppingSelection() = dao.clearShoppingSelection()

    suspend fun shoppingItemCount(userId: Int?): Int = dao.shoppingItemCount(userId)
}