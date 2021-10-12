package com.kdn.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kdn.presentation.R
import android.content.pm.PackageManager
import android.net.Uri
import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.camera.core.*
import androidx.camera.core.impl.PreviewConfig
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.jem.rubberpicker.RubberSeekBar
import com.kdn.presentation.databinding.ActivityMainBinding
import com.kdn.presentation.viewmodel.MainViewModel
import com.realpacific.clickshrinkeffect.applyClickShrink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this

        initViewModel()
    }

    private fun initViewModel(){
        viewModel.setCameraFlashLight(false)
    }
}