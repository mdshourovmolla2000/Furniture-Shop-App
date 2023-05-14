package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.model.ProductImageModel
import com.shourov.furnitureshop.utils.DemoData

class ProductDetailsRepository(private val dao: AppDao) {
    fun getProductImages(productId: String?): List<ProductImageModel?> = DemoData().productImageData().filter { it.productId == productId }
}