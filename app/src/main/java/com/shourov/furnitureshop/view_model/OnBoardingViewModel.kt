package com.shourov.furnitureshop.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.model.OnBoardingModel
import com.shourov.furnitureshop.repository.OnBoardingRepository

class OnBoardingViewModel(private val repository: OnBoardingRepository): ViewModel() {
    private val _onBoardingLiveData = MutableLiveData<ArrayList<OnBoardingModel>>()
    val onBoardingLiveData: LiveData<ArrayList<OnBoardingModel>>
        get() = _onBoardingLiveData

    fun getOnBoardingData() = _onBoardingLiveData.postValue(repository.getOnBoardingData())
}