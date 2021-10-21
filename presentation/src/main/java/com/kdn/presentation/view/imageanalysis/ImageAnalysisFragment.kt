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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.os.Build
import android.os.SystemClock
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kdn.presentation.viewmodel.ImageAnalysisViewModel
import com.kdn.presentation.widget.utils.Utils
import kotlinx.coroutines.Job
import org.pytorch.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class ImageAnalysisFragment : Fragment() {
    private val args by navArgs<ImageAnalysisFragmentArgs>()
    private lateinit var binding: FragmentImageAnalysisBinding

    //    private var inputTensor: Tensor? = null
    private val viewModel by activityViewModels<ImageAnalysisViewModel>()
    private var module: Module? = null
    private var MY_TORCHVISION_NORM_MEAN_RGB = floatArrayOf(0f, 0f, 0f)
    private var MY_TORCHVISION_NORM_STD_RGB = floatArrayOf(1f, 1f, 1f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_image_analysis, container, false)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val mutableBitmap = args.imageBitmap.copy(Bitmap.Config.RGBA_F16, true)

                // val resizedBitmap = Bitmap.createScaledBitmap(mutableBitmap, 512 , 512, true)
                //binding.img.setImageBitmap(resizedBitmap)
                module =
                    LiteModuleLoader.load(assetFilePath(requireContext(), "model_script.ptl"))

                Log.d("TAG", "분석됨1 ${args.imageBitmap}")
                val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
                    mutableBitmap,
                    MY_TORCHVISION_NORM_MEAN_RGB,
                    MY_TORCHVISION_NORM_STD_RGB,
                    MemoryFormat.CHANNELS_LAST
                )
                Log.d("TAG", "inputTensor : $inputTensor., ${inputTensor.dataAsFloatArray}")

                val moduleForwardStartTime = SystemClock.elapsedRealtime()
                val outputTensor = module?.forward(IValue.from(inputTensor))?.toTensor()

                val scores = outputTensor?.dataAsFloatArray

                for (index in 0 until scores?.size!!) {
                    Log.d("TAG", "outputTensor :  ${scores.get(index)}")

                }
                Log.d("TAG", "${Utils.topK(scores, 2)}, ${Utils.outputsToPredictions(scores)}")

            } catch (e: Exception) {
                Log.d("TAG", "imageAnalysisError : $e")
            }

        }


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "호출됨 destory")
        // job?.cancel()

    }

    private fun observeViewModel() {

    }
}