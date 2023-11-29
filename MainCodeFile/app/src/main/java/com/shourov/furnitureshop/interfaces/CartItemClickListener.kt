package com.shourov.furnitureshop.interfaces

import com.shourov.furnitureshop.database.tables.CartTable

interface CartItemClickListener {
    fun onCartItemClick(currentItem: CartTable?, clickedOn: String?)
}