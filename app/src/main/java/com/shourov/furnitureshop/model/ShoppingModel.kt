package com.shourov.furnitureshop.model

import com.shourov.furnitureshop.R

data class ShoppingModel(
    val itemImage: Int? = R.drawable.image_placeholder_square,
    val itemName: String? = "",
    val itemCompany: String? = "",
    val itemPrice: Double? = 0.0,
    var itemQuantity: Int? = 1,
    var isSelected: Boolean? = false
)