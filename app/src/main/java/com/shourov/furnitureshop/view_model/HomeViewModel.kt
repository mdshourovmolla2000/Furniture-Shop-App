package com.shourov.furnitureshop.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.model.SpecialOfferModel
import com.shourov.furnitureshop.repository.HomeRepository

class HomeViewModel(private val repository: HomeRepository): ViewModel() {
    private val _specialOfferLiveData = MutableLiveData<ArrayList<SpecialOfferModel>>()
    val specialOfferLiveData: LiveData<ArrayList<SpecialOfferModel>>
        get() = _specialOfferLiveData

    fun getSpecialOfferData() = _specialOfferLiveData.postValue(repository.getSpecialOfferData())

    fun getUserInfo(id: Int?): LiveData<UserTable?> = repository.getUserInfo(id)
}