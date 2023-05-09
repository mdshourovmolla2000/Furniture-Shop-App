package com.shourov.furnitureshop.interfaces

import com.shourov.furnitureshop.model.ShoppingModel

interface ShoppingItemClickListener {
    fun onShoppingItemClick(currentItem: ShoppingModel?, clickOn: String?)
}