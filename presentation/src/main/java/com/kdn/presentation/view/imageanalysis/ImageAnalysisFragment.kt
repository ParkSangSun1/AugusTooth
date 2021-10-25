package com.kdn.presentation.view.imageanalysis

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.kdn.presentation.R
import com.kdn.presentation.databinding.FragmentImageAnalysisBinding
import org.pytorch.torchvision.TensorImageUtils
import android.util.Log
import com.kdn.presentation.widget.utils.Utils.assetFilePath
import android.os.Build
import android.os.SystemClock
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kdn.presentation.viewmodel.ImageAnalysisViewModel
import com.kdn.presentation.widget.utils.Utils
import kotlinx.coroutines.*
import org.pytorch.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class ImageAnalysisFragment : Fragment() {
    private val args by navArgs<ImageAnalysisFragmentArgs>()
    private lateinit var binding: FragmentImageAnalysisBinding
    private val viewModel by activityViewModels<ImageAnalysisViewModel>()
    private var module: Module? = null
    private lateinit var mutableBitmap: Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        viewModel.setAnalysisImageResponse(null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_analysis, container, false)
        observeViewModel()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                readingImage()
                loadingModule()
                viewModel.analysisImage(mutableBitmap, module)
            } catch (e: Exception) {
                Log.d("TAG", "imageAnalysisError : $e")
            }

        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun readingImage() {
        mutableBitmap = args.imageBitmap.copy(Bitmap.Config.RGBA_F16, true)
    }

    private suspend fun loadingModule(){
        module = LiteModuleLoader.load(assetFilePath(requireContext(), "model_script.ptl"))

    }

    private fun observeViewModel() {
        viewModel.analysisImageResponse.observe(requireActivity(), Observer {
            Log.d("로그", "뷰모델 호출됨")
            if (it != null) binding.imageView.visibility = View.GONE
            else binding.imageView.visibility = View.VISIBLE
        })
    }
}