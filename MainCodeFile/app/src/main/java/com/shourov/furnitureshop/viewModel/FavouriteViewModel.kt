package com.shourov.furnitureshop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shourov.furnitureshop.database.tables.FavouriteTable
import com.shourov.furnitureshop.repository.FavouriteRepository
import kotlinx.coroutines.launch

class FavouriteViewModel(private val repository: FavouriteRepository): ViewModel() {
    fun getFavouriteData(userId: Int?): LiveData<List<FavouriteTable>> = repository.getFavouriteData(userId)

    fun getProductDetails(productId: String?) =repository.getProductDetails(productId)

    fun deleteFavourite(favourite: FavouriteTable) = viewModelScope.launch { repository.deleteFavourite(favourite) }
}