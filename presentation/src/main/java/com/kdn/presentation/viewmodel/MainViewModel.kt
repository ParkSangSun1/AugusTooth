package com.kdn.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val cameraAutoCheck : LiveData<Boolean> get() = _cameraAutoCheck
    private val _cameraAutoCheck = MutableLiveData<Boolean>()

    //플래시 불빛 킬건지 설정정
   val cameraFlashLight : LiveData<Boolean> get() = _cameraFlashLight
    private val _cameraFlashLight = MutableLiveData<Boolean>()

    fun setCameraAutoCheck(check : Boolean){
        _cameraAutoCheck.value = check
    }

    fun setCameraFlashLight(set : Boolean){
        _cameraFlashLight.value = set
    }
}