package com.shourov.furnitureshop.interfaces

import com.shourov.furnitureshop.model.ProductModel

interface PopularProductItemClickListener {
    fun onProductItemClick(currentItem: ProductModel)
}