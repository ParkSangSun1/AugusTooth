package com.kdn.presentation.view.cameramain

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
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
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.jem.rubberpicker.RubberSeekBar
import com.kdn.presentation.R
import com.kdn.presentation.databinding.ActivityMainBinding
import com.kdn.presentation.databinding.FragmentCameraBinding
import com.kdn.presentation.viewmodel.MainViewModel
import com.realpacific.clickshrinkeffect.applyClickShrink
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private val TAG = "로그"
    private val OPEN_GALLERY = 1
    private var camera: Camera? = null
    private val viewModel by activityViewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false)
        binding.fragment = this
        initColor()
       // checkAuth()
        initCamera()
        observeViewModel()
        startCamera()
        return binding.root
    }

    private fun initColor() {
        binding.backgroundCircle.setColorFilter(Color.parseColor("#F98484"))
    }

    private fun checkAuth() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun initCamera() {
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    //권한승인 요청 or 권한확인
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                Log.d("로그","여기 왔다이")
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "설정에서 카메라 권한을 승인해 주세요",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //설정 버튼 클릭
    fun settingBtn(view: View) {
        view.findNavController().navigate(R.id.action_cameraFragment_to_cameraAutoFragment)
    }

    //플래시 기능 켜져있는지 확인
    private fun checkFlashLightState() {
        if (viewModel.cameraFlashLight.value!!) imageCapture?.flashMode = FLASH_MODE_ON
        else imageCapture?.flashMode = FLASH_MODE_OFF
    }

    //플래시 버튼 클릭
    fun flashLightBtn(view: View) {
        if (viewModel.cameraFlashLight.value!!) viewModel.setCameraFlashLight(false)
        else viewModel.setCameraFlashLight(true)
    }

    private fun observeViewModel() {
        viewModel.cameraFlashLight.observe(requireActivity(), androidx.lifecycle.Observer {
            checkFlashLightState(it)
        })
    }

    private fun checkFlashLightState(it: Boolean) {
        if (it) binding.flashlightBtn.setImageResource(R.drawable.flashlight_false)
        else binding.flashlightBtn.setImageResource(R.drawable.flashlight_true)
    }


    //사진 버튼 클릭
    fun takePhoto(view: View) {
        Log.d("로기", "찍힘")

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

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val mediaScanIntent: Intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                    val contentUri: Uri = Uri.fromFile(photoFile)
                    mediaScanIntent.data = contentUri
                    activity?.sendBroadcast(mediaScanIntent)
                }
            })
    }

    //전면, 후면 카메라 전환
    fun changeCamera(view: View) {
        binding.change.applyClickShrink()
        Log.d(TAG, "눌림")
        lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
            CameraSelector.LENS_FACING_BACK
        } else {
            CameraSelector.LENS_FACING_FRONT
        }
        startCamera()
    }

    //갤러리에서 사진 불러오기
    fun galleryBtn(view: View) {
        binding.gallery.applyClickShrink()
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

                //seekbar 줌 리스너 추가
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


            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
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

    //갤러리에서 사진 선택
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_GALLERY) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                //선택한 이미지의 데이터가 이곳에 저장
                var currentImageUri = data?.data

                try {
                    currentImageUri?.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver,
                                currentImageUri
                            )
                            this.findNavController().navigate(R.id.action_cameraFragment_to_imageAnalysisFragment)
//                            imageView?.setImageBitmap(bitmap)
                        } else {
                            val source =
                                ImageDecoder.createSource(
                                    requireContext().contentResolver,
                                    currentImageUri
                                )
                            val bitmap = ImageDecoder.decodeBitmap(source)
//                            imageView?.setImageBitmap(bitmap)
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


    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}