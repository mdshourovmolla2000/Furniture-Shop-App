package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.model.CategoryModel
import com.shourov.furnitureshop.utils.DemoData

class CategoryRepository() {
    fun getCategory(): List<CategoryModel> = DemoData().categoryData()

    fun categoryProductCount(categoryName: String?): Int = DemoData().productData().count { it.itemCategory == categoryName }
}