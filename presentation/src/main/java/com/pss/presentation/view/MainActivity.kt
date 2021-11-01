package com.pss.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pss.presentation.R
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.pss.presentation.base.BaseActivity
import com.pss.presentation.databinding.ActivityMainBinding
import com.pss.presentation.viewmodel.CameraMainViewModel


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel by viewModels<CameraMainViewModel>()

    override fun init() {
        binding.apply {
            activity = this@MainActivity
        }
        initViewModel()
    }

    private fun initViewModel(){
        viewModel.setCameraFlashLight(false)
    }


}