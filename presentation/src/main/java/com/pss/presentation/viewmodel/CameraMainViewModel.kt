package com.pss.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pss.presentation.base.BaseViewModel
import com.pss.presentation.widget.utils.SingleLiveEvent

class CameraMainViewModel : BaseViewModel() {
/*    //사진 촬영 버튼 눌렀는지
    val clickTakePhoto : LiveData<Boolean> get() = _clickTakePhoto
    private val _clickTakePhoto = MutableLiveData<Boolean>()
    */

    companion object{
        const val EVENT_START_ANALYSIS = 1
    }

    fun startAnalysisActivity() = viewEvent(EVENT_START_ANALYSIS)

    //사진 촬영 버튼 눌렀는지
    val clickTakePhoto : LiveData<Any> get() = _clickTakePhoto
    private val _clickTakePhoto = SingleLiveEvent<Any>()

    //플래시 불빛 킬건지 설정정
   val cameraFlashLight : LiveData<Boolean> get() = _cameraFlashLight
    private val _cameraFlashLight = MutableLiveData<Boolean>()


    fun setCameraFlashLight(set : Boolean){
        _cameraFlashLight.value = set
    }

    fun setClickTakePhoto(){
        _clickTakePhoto.call()
    }
}