package com.kdn.presentation.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ImageAnalysisViewModel(
    private val app: Application,
  //  private val imageAnalysis: ImageAnalysis
    )
    : AndroidViewModel(app){
    val imageBitmap : LiveData<Bitmap> get() = _imageBitmap
    private val _imageBitmap = MutableLiveData<Bitmap>()

    val analysisText : LiveData<String> get() = _analysisText
    private val _analysisText = MutableLiveData<String>()

    fun setImageBitmap(image : Bitmap){
        _imageBitmap.value = image
    }

    fun setAnalysisText(txt: String){
        _analysisText.value = txt
    }
}