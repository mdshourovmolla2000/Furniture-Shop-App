package com.shourov.furnitureshop.repository

import androidx.lifecycle.LiveData
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.CartTable

class CartRepository(private val dao: AppDao) {
    fun getCartData(userId: Int?): LiveData<List<CartTable>> = dao.getCartData(userId)

    suspend fun updateCart(cart: CartTable) = dao.updateCart(cart)

    suspend fun deleteCart(cartList: List<CartTable>) = dao.deleteCart(cartList)

    suspend fun clearCartSelection() = dao.clearCartSelection()

    suspend fun cartItemCount(userId: Int?): Int = dao.cartItemCount(userId)
}