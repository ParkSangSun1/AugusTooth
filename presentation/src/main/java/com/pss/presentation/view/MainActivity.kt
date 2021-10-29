package com.pss.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pss.presentation.R
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.pss.presentation.databinding.ActivityMainBinding
import com.pss.presentation.viewmodel.CameraMainViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<CameraMainViewModel>()

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