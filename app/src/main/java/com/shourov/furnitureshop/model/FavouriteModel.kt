package com.shourov.furnitureshop.model

import com.shourov.furnitureshop.R

data class FavouriteModel(
    val itemImage: Int? = R.drawable.image_placeholder_square,
    val itemName: String? = "",
    val itemPrice: String? = ""
)