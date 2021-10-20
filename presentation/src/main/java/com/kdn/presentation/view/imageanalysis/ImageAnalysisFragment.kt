package com.kdn.presentation.view.imageanalysis

import android.R.attr
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.kdn.presentation.R
import com.kdn.presentation.databinding.FragmentImageAnalysisBinding
import android.graphics.BitmapFactory
import org.pytorch.Module
import org.pytorch.Tensor
import org.pytorch.torchvision.TensorImageUtils
import java.io.File
import android.R.attr.bitmap
import android.content.Context
import android.util.Log
import com.kdn.presentation.widget.utils.Utils.assetFilePath
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.pytorch.IValue
import org.pytorch.LiteModuleLoader
import java.io.FileOutputStream
import java.io.IOException
import kotlin.concurrent.thread
import android.R.attr.bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.SystemClock
import androidx.annotation.RequiresApi


class ImageAnalysisFragment : Fragment() {

    private val args by navArgs<ImageAnalysisFragmentArgs>()
    private lateinit var binding: FragmentImageAnalysisBinding
    private var inputTensor: Tensor? = null

    //private var moduleAssetName: String = "model_whole.pt"
    private var module: Module? = null


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

                module =
                    LiteModuleLoader.load(assetFilePath(requireContext(), "model_resnet.ptl"))

                Log.d("TAG", "분석됨1 ${args.imageBitmap}")
                inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
                    mutableBitmap,
                    TensorImageUtils.TORCHVISION_NORM_MEAN_RGB,
                    TensorImageUtils.TORCHVISION_NORM_STD_RGB
                )
                val moduleForwardStartTime = SystemClock.elapsedRealtime()
                val outputTensor = module?.forward(IValue.from(inputTensor))?.toTensor()

                val moduleForwardDuration = SystemClock.elapsedRealtime() - moduleForwardStartTime
                Log.d("TAG", "outputTensor : $outputTensor}, $moduleForwardDuration")

            } catch (e: Exception) {
                Log.d("TAG", "ImageAnalysisError : $e")
            }

        }

        return binding.root
    }
}