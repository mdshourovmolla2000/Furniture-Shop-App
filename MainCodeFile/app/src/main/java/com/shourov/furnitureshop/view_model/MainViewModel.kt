package com.shourov.furnitureshop.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shourov.furnitureshop.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {
    private val _loadingDialogLiveData = MutableLiveData<Boolean?>()
    val loadingDialogLiveData: LiveData<Boolean?> get() = _loadingDialogLiveData
    fun setLoadingDialog(isLoading: Boolean?) = viewModelScope.launch { _loadingDialogLiveData.postValue(repository.getLoadingDialog(isLoading)) }

    private val _loadingDialogTextLiveData = MutableLiveData<String?>()
    val loadingDialogTextLiveData: LiveData<String?> get() = _loadingDialogTextLiveData
    fun setLoadingDialogText(title: String?) = viewModelScope.launch { _loadingDialogTextLiveData.postValue(repository.getLoadingDialogText(title)) }
}