package com.pss.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pss.presentation.R
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.pss.presentation.base.BaseActivity
import com.pss.presentation.databinding.ActivityMainBinding
import com.pss.presentation.viewmodel.CameraMainViewModel
import com.pss.presentation.widget.utils.DataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel by viewModels<CameraMainViewModel>()

    override fun init() {
        binding.apply {
            activity = this@MainActivity
        }
        initViewModel()
        startLocationCheck()
    }

    private fun startLocationCheck() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.readLocationInDataStore().apply {
                if (this == DataStore.DEFAULT_LOCATION) longShowToast("위에 위치 아이콘 또는 설정에서 위치를 설정해 주세요")
                else shortShowToast("현재 설정된 위치는 $this 입니다")
            }
        }

    }

    private fun initViewModel() {
        viewModel.setCameraFlashLight(false)
    }
}