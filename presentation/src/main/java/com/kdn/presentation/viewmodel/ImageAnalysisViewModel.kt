package com.kdn.presentation.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ImageAnalysisViewModel {
    val imageBitmap : LiveData<Bitmap> get() = _imageBitmap
    private val _imageBitmap = MutableLiveData<Bitmap>()

    fun setImageBitmap(image : Bitmap){
        _imageBitmap.value = image
    }
}