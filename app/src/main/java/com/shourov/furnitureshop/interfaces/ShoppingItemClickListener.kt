package com.shourov.furnitureshop.interfaces

import com.shourov.furnitureshop.database.tables.ShoppingTable

interface ShoppingItemClickListener {
    fun onShoppingItemClick(currentItem: ShoppingTable?, clickOn: String?)
}