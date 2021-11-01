package com.pss.presentation.view.cameramain

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.fragment.findNavController
import com.pss.presentation.R
import com.pss.presentation.viewmodel.CameraMainViewModel

class Listeners(
    private val fragment: CameraFragment,
    private val viewModel: CameraMainViewModel
) {
    //설정 버튼 클릭
    fun settingBtn() = fragment.findNavController().navigate(R.id.action_cameraFragment_to_cameraAutoFragment)


    //플래시 버튼 클릭
    fun flashLightBtn() {
        if (viewModel.cameraFlashLight.value!!) viewModel.setCameraFlashLight(false)
        else viewModel.setCameraFlashLight(true)
    }


    //사진 촬영 버튼 클릭
    fun takePhoto() = viewModel.startAnalysisActivity()


    //갤러리 버튼 클릭
    fun galleryBtn() = viewModel.startGalleryIntent()


    //전면, 후면 카메라 전환 버튼 클릭
    fun changeCameraBtn() = viewModel.changeCamera()


    //위치설정 버튼 클릭
    fun locationBtn() = viewModel.startLocationFragment()
}