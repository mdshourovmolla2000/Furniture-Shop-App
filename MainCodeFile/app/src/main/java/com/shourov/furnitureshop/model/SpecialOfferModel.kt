package com.shourov.furnitureshop.model

import com.shourov.furnitureshop.R

data class SpecialOfferModel(
    val discountTitle: String? = "",
    val discountTagline: String? = "",
    val actionText: String? = "",
    val itemImage: Int? = R.drawable.loading_image
)