package com.shourov.furnitureshop.interfaces

import com.shourov.furnitureshop.model.ShoppingModel

interface HomeCategoryItemClickListener {
    fun onCategoryItemClick(currentItem: String, currentItemPosition: Int)
}