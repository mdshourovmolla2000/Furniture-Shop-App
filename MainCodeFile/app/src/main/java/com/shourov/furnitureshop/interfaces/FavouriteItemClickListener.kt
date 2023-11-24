package com.shourov.furnitureshop.interfaces

import android.widget.ImageView
import android.widget.TextView
import com.shourov.furnitureshop.database.tables.FavouriteTable

interface FavouriteItemClickListener {
    fun onLoadFavouriteItem(currentProductId: String?, itemImageImageview: ImageView?, itemNameTextview: TextView?, itemPriceTextview: TextView?)

    fun onFavouriteItemClick(currentItem: FavouriteTable, clickedOn: String?)
}