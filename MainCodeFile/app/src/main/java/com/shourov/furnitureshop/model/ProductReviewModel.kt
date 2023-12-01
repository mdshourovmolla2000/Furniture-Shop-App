package com.shourov.furnitureshop.model

data class ProductReviewModel(
    val id: String? = "1",
    val productId: String? = "",
    val userName: String? = "",
    val reviewDateAndTime: String? = "",
    val reviewRating: Int? = 5,
    val reviewImages: List<Int>?,
    val reviewDetails: String? = "",
)