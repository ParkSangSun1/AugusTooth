package com.kdn.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val cameraAutoCheck : LiveData<Boolean> get() = _cameraAutoCheck
    private val _cameraAutoCheck = MutableLiveData<Boolean>()

    fun setCameraAutoCheck(check : Boolean){
        _cameraAutoCheck.value = check
    }
}