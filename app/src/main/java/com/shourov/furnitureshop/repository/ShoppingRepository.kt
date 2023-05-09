package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.model.ShoppingModel
import com.shourov.furnitureshop.utils.DemoData

class ShoppingRepository {
    fun getShoppingData(): ArrayList<ShoppingModel> = DemoData().shoppingData()
}