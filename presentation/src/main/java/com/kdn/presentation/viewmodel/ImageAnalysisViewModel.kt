package com.kdn.presentation.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.*
import com.kdn.presentation.widget.utils.Utils
import com.kdn.presentation.widget.utils.Utils.outputResultComparison
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.pytorch.*
import org.pytorch.torchvision.TensorImageUtils

class ImageAnalysisViewModel() : ViewModel() {
    val imageBitmap: LiveData<Bitmap> get() = _imageBitmap
    private val _imageBitmap = MutableLiveData<Bitmap>()

    val analysisImageResponse: LiveData<Int?> get() = _analysisImageResponse
    private val _analysisImageResponse = MutableLiveData<Int?>()

    private var MY_TORCHVISION_NORM_MEAN_RGB = floatArrayOf(0f, 0f, 0f)
    private var MY_TORCHVISION_NORM_STD_RGB = floatArrayOf(1f, 1f, 1f)

    init {
        _analysisImageResponse.value = null
    }

    fun setImageBitmap(image: Bitmap) {
        _imageBitmap.value = image
    }

    fun setAnalysisImageResponse(response: Int?) {
        _analysisImageResponse.value = response
    }


    fun analysisImage(bitmap: Bitmap, module: Module?) = viewModelScope.launch {
        var scores : FloatArray? = null
        withContext(Dispatchers.Default){
            try {
                val inputTensor = preparingInput(bitmap)
                val outputTensor = runInference(module = module, inputTensor = inputTensor)
                scores = outputTensor?.dataAsFloatArray

                for (index in scores!!.indices) {
                    Log.d("TAG", "outputTensor :  ${scores!![index]}")
                }

            } catch (e: Exception) {
                Log.d("TAG", "imageAnalysisError : $e")
            }
        }
        _analysisImageResponse.value = scores?.let { outputResultComparison(it) }

    }

    private suspend fun preparingInput(bitmap: Bitmap) = TensorImageUtils.bitmapToFloat32Tensor( bitmap, MY_TORCHVISION_NORM_MEAN_RGB, MY_TORCHVISION_NORM_STD_RGB, MemoryFormat.CHANNELS_LAST)

    private suspend fun runInference(module: Module?, inputTensor : Tensor) = module?.forward(IValue.from(inputTensor))?.toTensor()
}