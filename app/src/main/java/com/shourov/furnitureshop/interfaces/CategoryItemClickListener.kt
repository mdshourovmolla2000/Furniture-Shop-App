package com.shourov.furnitureshop.interfaces

import android.widget.TextView
import com.shourov.furnitureshop.model.CategoryModel

interface CategoryItemClickListener {
    fun onLoadCategoryItem(currentItem: CategoryModel, categoryProductCountTextview: TextView?)

    fun onCategoryItemClick(categoryName: String)
}