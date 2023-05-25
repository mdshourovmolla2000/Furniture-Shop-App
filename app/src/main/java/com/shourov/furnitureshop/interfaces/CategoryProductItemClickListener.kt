package com.shourov.furnitureshop.interfaces

import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.shourov.furnitureshop.model.ProductModel

interface CategoryProductItemClickListener {
    fun onLoadProductItem(currentItem: ProductModel, cartIconCardView: CardView, cartIconImageview: ImageView)

    fun onProductItemClick(currentItem: ProductModel, cartIconCardView: CardView, clickOn: String?)
}