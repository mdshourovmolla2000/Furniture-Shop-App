package com.shourov.furnitureshop.interfaces

import com.shourov.furnitureshop.model.FavouriteModel

interface FavouriteItemClickListener {
    fun onFavouriteItemClick(currentItem: FavouriteModel?, clickOn: String?)
}