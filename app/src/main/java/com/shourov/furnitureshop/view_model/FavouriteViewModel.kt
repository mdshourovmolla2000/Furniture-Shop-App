package com.shourov.furnitureshop.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.model.FavouriteModel
import com.shourov.furnitureshop.repository.FavouriteRepository

class FavouriteViewModel(private val repository: FavouriteRepository): ViewModel() {
    private val _favouriteLiveData = MutableLiveData<ArrayList<FavouriteModel>>()
    val favouriteLiveData: LiveData<ArrayList<FavouriteModel>>
        get() = _favouriteLiveData

    fun getFavouriteData() = _favouriteLiveData.postValue(repository.getFavouriteData())
}