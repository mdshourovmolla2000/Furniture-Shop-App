package com.shourov.furnitureshop.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.model.SpecialOfferData
import com.shourov.furnitureshop.repository.HomeRepository

class HomeViewModel(private val repository: HomeRepository): ViewModel() {
    private val _specialOfferLiveData = MutableLiveData<ArrayList<SpecialOfferData>>()
    val specialOfferLiveData: LiveData<ArrayList<SpecialOfferData>>
        get() = _specialOfferLiveData

    fun getSpecialOfferLiveData() = _specialOfferLiveData.postValue(repository.getSpecialOfferData())
}