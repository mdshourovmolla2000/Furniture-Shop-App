package com.shourov.furnitureshop.interfaces

import androidx.recyclerview.widget.RecyclerView
import com.shourov.furnitureshop.model.ProductReviewModel

interface ProductReviewItemClickListener {
    fun onLoadReviewItem(currentItem: ProductReviewModel?, reviewImageRecyclerview: RecyclerView?)
}