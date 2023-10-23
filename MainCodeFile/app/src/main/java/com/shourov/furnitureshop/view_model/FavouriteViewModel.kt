package com.shourov.furnitureshop.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.database.tables.FavouriteTable
import com.shourov.furnitureshop.repository.FavouriteRepository

class FavouriteViewModel(private val repository: FavouriteRepository): ViewModel() {
    fun getFavouriteData(userId: Int?): LiveData<List<FavouriteTable?>?> = repository.getFavouriteData(userId)

    fun getProductDetails(productId: String?) =repository.getProductDetails(productId)

    suspend fun deleteFavourite(favourite: FavouriteTable?) = repository.deleteFavourite(favourite)
}