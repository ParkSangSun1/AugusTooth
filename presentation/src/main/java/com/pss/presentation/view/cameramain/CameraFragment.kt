package com.pss.presentation.view.cameramain

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.jem.rubberpicker.RubberSeekBar
import com.pss.presentation.R
import com.pss.presentation.base.BaseFragment
import com.pss.presentation.databinding.FragmentCameraBinding
import com.pss.presentation.viewmodel.CameraMainViewModel
import com.pss.presentation.viewmodel.CameraMainViewModel.Companion.EVENT_CHANGE_CAMERA
import com.pss.presentation.viewmodel.CameraMainViewModel.Companion.EVENT_START_ANALYSIS
import com.pss.presentation.viewmodel.CameraMainViewModel.Companion.EVENT_START_GALLERY
import com.pss.presentation.viewmodel.CameraMainViewModel.Companion.EVENT_START_LOCATION_SETTING
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : BaseFragment<FragmentCameraBinding>(R.layout.fragment_camera) {
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private val TAG = "로그"
    private val OPEN_GALLERY = 1
    private val viewModel by activityViewModels<CameraMainViewModel>()
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"


    override fun init() {
        binding.fragment = this
        binding.listeners = Listeners(this, viewModel)
        initColor()
        initCamera()
        observeViewModel()
        startCamera()
    }

    private fun initColor() {
        binding.backgroundCircle.setColorFilter(Color.parseColor("#F98484"))
    }

    private fun initCamera() {
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    //플래시 기능 켜져있는지 확인
    private fun checkFlashLightState() {
        if (viewModel.cameraFlashLight.value!!) imageCapture?.flashMode = FLASH_MODE_ON
        else imageCapture?.flashMode = FLASH_MODE_OFF
    }

    private fun observeViewModel() {
        viewModel.cameraFlashLight.observe(requireActivity(), androidx.lifecycle.Observer {
            checkFlashLightState(it)
        })

        viewModel.viewEvent.observe(requireActivity(),{
            it.getContentIfNotHandled()?.let { event ->
                when(event){
                    EVENT_START_ANALYSIS -> takePhoto()
                    EVENT_START_GALLERY -> startGallery()
                    EVENT_CHANGE_CAMERA -> changeCamera()
                    EVENT_START_LOCATION_SETTING -> startLocationSettingFragment()
                }
            }
        })
    }

    private fun checkFlashLightState(it: Boolean) {
        if (it) binding.flashlightBtn.setImageResource(R.drawable.flashlight_false)
        else binding.flashlightBtn.setImageResource(R.drawable.flashlight_true)
    }

    private fun takePhoto() {
        checkFlashLightState()

        imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.KOREA
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture!!.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                @RequiresApi(Build.VERSION_CODES.P)
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val mediaScanIntent: Intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                    val contentUri: Uri = Uri.fromFile(photoFile)
                    mediaScanIntent.data = contentUri
                    activity?.sendBroadcast(mediaScanIntent)

                    analysisNavController(bitmapConversion(contentUri, false))
                }
            })
    }

    //sdk < 28 = false, sdk >= 28 = true
    //bitmap으로 uri변환
    @RequiresApi(Build.VERSION_CODES.P)
    private fun bitmapConversion(uri: Uri, boolean: Boolean): Bitmap {
        return if (boolean) MediaStore.Images.Media.getBitmap(
            requireContext().contentResolver,
            uri
        )
        else ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                requireContext().contentResolver,
                uri
            )
        )
    }

    private fun changeCamera() {
        lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) CameraSelector.LENS_FACING_BACK
        else CameraSelector.LENS_FACING_FRONT
        startCamera()
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, OPEN_GALLERY)
    }

    //카메라 시작
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                        Log.d(TAG, "Average luminosity: $luma")
                    })
                }


            //val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

            try {
                cameraProvider.unbindAll()
                val cameraControl = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
                )
                initZoomListener(cameraControl)

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    //seekbar 줌 리스너 추가
    private fun initZoomListener(cameraControl: Camera){
        binding.zoomSeekBar.setOnRubberSeekBarChangeListener(object :
            RubberSeekBar.OnRubberSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: RubberSeekBar,
                value: Int,
                fromUser: Boolean
            ) {
                cameraControl.cameraControl.setLinearZoom(value / 100.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: RubberSeekBar) {
                binding.zoomTxt.visibility = View.INVISIBLE
            }

            override fun onStopTrackingTouch(seekBar: RubberSeekBar) {
                binding.zoomTxt.visibility = View.VISIBLE
            }
        })
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else activity?.filesDir!!
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun startLocationSettingFragment() {
        shortShowToast("해당 기능은 사용할 수 없습니다")
        //this.findNavController().navigate(R.id.action_cameraFragment_to_locationFragment)
    }

    //갤러리에서 사진 선택
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_GALLERY) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                //선택한 이미지의 데이터가 이곳에 저장
                var currentImageUri = data?.data

                try {
                    currentImageUri?.let {
                        if (Build.VERSION.SDK_INT < 28) {

                            analysisNavController(bitmapConversion(currentImageUri, false))
                        } else {

                            analysisNavController(bitmapConversion(currentImageUri, true))
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                Log.d("로그", "사진 취소됨")
            }
        }
    }

    private fun analysisNavController(bitmap: Bitmap) = this.findNavController().navigate(CameraFragmentDirections.actionCameraFragmentToImageAnalysisFragment(bitmap))
}