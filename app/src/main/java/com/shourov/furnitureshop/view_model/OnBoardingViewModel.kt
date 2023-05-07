package com.shourov.furnitureshop.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shourov.furnitureshop.model.OnBoardingData
import com.shourov.furnitureshop.repository.OnBoardingRepository

class OnBoardingViewModel(private val repository: OnBoardingRepository): ViewModel() {
    private val _onBoardingLiveData = MutableLiveData<ArrayList<OnBoardingData>>()
    val onBoardingLiveData: LiveData<ArrayList<OnBoardingData>>
        get() = _onBoardingLiveData

    fun getOnBoardingData() = _onBoardingLiveData.postValue(repository.getOnBoardingData())
}