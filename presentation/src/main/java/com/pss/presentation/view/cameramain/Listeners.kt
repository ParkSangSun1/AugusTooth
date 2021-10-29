package com.pss.presentation.view.cameramain

import android.content.Intent
import android.view.View
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.fragment.findNavController
import com.pss.presentation.R
import com.pss.presentation.viewmodel.CameraMainViewModel

class Listeners(
    private val fragment: CameraFragment,
    private val viewModel : CameraMainViewModel
    ) {


    //설정 버튼 클릭
    fun settingBtn() {
        fragment.findNavController().navigate(R.id.action_cameraFragment_to_cameraAutoFragment)
    }

    //플래시 버튼 클릭
    fun flashLightBtn() {
        if (viewModel.cameraFlashLight.value!!) viewModel.setCameraFlashLight(false)
        else viewModel.setCameraFlashLight(true)
    }

    fun takePhoto(){
        viewModel.startAnalysisActivity()
    }

}