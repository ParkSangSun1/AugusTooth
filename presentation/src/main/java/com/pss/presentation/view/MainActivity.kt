package com.pss.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pss.presentation.R
import androidx.activity.viewModels
import androidx.camera.core.*
import androidx.databinding.DataBindingUtil
import com.pss.presentation.databinding.ActivityMainBinding
import com.pss.presentation.viewmodel.MainViewModel
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

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