package com.pss.presentation.view.imageanalysis

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.pss.presentation.R
import com.pss.presentation.databinding.FragmentImageAnalysisBinding
import android.util.Log
import com.pss.presentation.widget.utils.Utils.assetFilePath
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pss.presentation.base.BaseFragment
import com.pss.presentation.viewmodel.ImageAnalysisViewModel
import kotlinx.coroutines.*
import org.pytorch.*


class ImageAnalysisFragment :
    BaseFragment<FragmentImageAnalysisBinding>(R.layout.fragment_image_analysis) {
    private val args by navArgs<ImageAnalysisFragmentArgs>()
    private val viewModel by activityViewModels<ImageAnalysisViewModel>()
    private var module: Module? = null
    private lateinit var mutableBitmap: Bitmap
    private lateinit var job: Job

    override fun onStart() {
        super.onStart()
        viewModel.setAnalysisImageResponse(null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun readingImage() {
        mutableBitmap = args.imageBitmap.copy(Bitmap.Config.RGBA_F16, true)
    }

    private suspend fun loadingModule() {
        module = LiteModuleLoader.load(assetFilePath(requireContext(), "model_script.ptl"))

    }

    fun backBtn(view: View) {
        this.findNavController().popBackStack()
    }

    private fun observeViewModel() {
        viewModel.analysisImageResponse.observe(requireActivity(), Observer {
            if (it != null) {
                binding.imageView.visibility = View.GONE
                binding.layout.visibility = View.VISIBLE
                binding.resultTxt.text = when (viewModel.analysisImageResponse.value) {
                    0 -> "교정이 필요하지 않습니다"
                    1 -> "교정이 필요합니다"
                    else -> "분석에 실패했습니다"
                }
            } else {
                binding.imageView.visibility = View.VISIBLE
                binding.layout.visibility = View.INVISIBLE
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun init() {
        binding.fragment = this
        longShowToast("사진을 분석하는데 약 1~2분이 걸릴수 있습니다")
        observeViewModel()
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                readingImage()
                loadingModule()
                viewModel.analysisImage(mutableBitmap, module)
            } catch (e: Exception) {
                Log.d("TAG", "imageAnalysisError : $e")
            }

        }
    }
}