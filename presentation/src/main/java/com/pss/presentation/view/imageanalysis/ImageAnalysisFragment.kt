package com.pss.presentation.view.imageanalysis

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pss.presentation.base.BaseFragment
import com.pss.presentation.viewmodel.ImageAnalysisViewModel
import com.pss.presentation.widget.utils.DataStore.DEFAULT_LOCATION
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
    override fun init() {
        binding.fragment = this
        longShowToast("사진을 분석하는데 약 1~2분이 걸릴수 있습니다")
        observeViewModel()
        job = lifecycleScope.launch(Dispatchers.IO) {
            try {
                readingImage()
                loadingModule()
                viewModel.analysisImage(mutableBitmap, module)
            } catch (e: Exception) {
                longShowToast("사진을 분석하는데 오류가 발생했습니다")
            }
        }
    }

    //이미지 bitmap 으로 변환
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun readingImage() {
        withContext(Dispatchers.Default){
            mutableBitmap = args.imageBitmap.copy(Bitmap.Config.RGBA_F16, true)
        }
    }

    //모듈로 분석
    private suspend fun loadingModule() {
        withContext(Dispatchers.Default) {
            module = LiteModuleLoader.load(assetFilePath(requireContext(), "model_script.ptl"))
        }
    }

    //뒤로가기 버튼
    fun backBtn(view: View) {
        job.cancel()
        this.findNavController().popBackStack()
    }

    //주변 치과 찾아보기 버튼 클릭
    fun seeNearbyDentistBtnClick(view: View) {
        lifecycleScope.launch(Dispatchers.IO) {
            val location = viewModel.readLocationInDataStore()
            if (location == DEFAULT_LOCATION) withContext(Dispatchers.Main) {
                shortShowToast("먼저 위치를 설정해 주세요")
            }
            else {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.data = Uri.parse("geo:0,0?q=${location}치과")
                startActivity(intent)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.analysisImageResponse.observe(requireActivity(), Observer {
            if (it != null) {
                resultVisible()
            } else {
                resultInvisible()
            }
        })
    }

    //결과 표시 보이기
    private fun resultVisible(){
        binding.imageView.visibility = View.GONE
        binding.layout.visibility = View.VISIBLE
        binding.resultTxt.text = when (viewModel.analysisImageResponse.value) {
            0 -> {
                binding.searchDentist.visibility = View.GONE
                "교정이 필요하지 않습니다"
            }
            1 -> "교정이 필요합니다"
            else -> "분석에 실패했습니다"
        }
    }

    //결과 표시 숨기기
    private fun resultInvisible(){
        binding.imageView.visibility = View.VISIBLE
        binding.layout.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}