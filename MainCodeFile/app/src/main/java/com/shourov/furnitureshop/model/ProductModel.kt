package com.shourov.furnitureshop.model

import com.shourov.furnitureshop.R

data class ProductModel(
    val id: String? = "1",
    val itemImage: Int? = R.drawable.loading_image,
    val itemName: String? = "",
    val itemCompanyName: String? = "",
    val itemDescription: String? = "",
    val itemCategory: String? = "",
    val itemPrice: Double? = 0.0
)