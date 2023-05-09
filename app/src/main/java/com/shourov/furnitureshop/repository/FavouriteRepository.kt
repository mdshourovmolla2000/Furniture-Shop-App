package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.model.FavouriteModel
import com.shourov.furnitureshop.utils.DemoData

class FavouriteRepository {
    fun getFavouriteData(): ArrayList<FavouriteModel> = DemoData().favouriteData()
}