package com.pss.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pss.presentation.base.BaseViewModel
import com.pss.presentation.widget.utils.SingleLiveEvent

class CameraMainViewModel : BaseViewModel() {

    companion object{
        const val EVENT_START_ANALYSIS = 1
        const val EVENT_START_GALLERY = 2
        const val EVENT_CHANGE_CAMERA = 3
        const val EVENT_START_LOCATION_SETTING = 4
    }

    //플래시 불빛 킬건지 설정정
   val cameraFlashLight : LiveData<Boolean> get() = _cameraFlashLight
    private val _cameraFlashLight = MutableLiveData<Boolean>()


    fun setCameraFlashLight(set : Boolean){
        _cameraFlashLight.value = set
    }

    fun startAnalysisActivity() = viewEvent(EVENT_START_ANALYSIS)

    fun startGalleryIntent() = viewEvent(EVENT_START_GALLERY)

    fun changeCamera() = viewEvent(EVENT_CHANGE_CAMERA)

    fun startLocationFragment() = viewEvent(EVENT_START_LOCATION_SETTING)
}