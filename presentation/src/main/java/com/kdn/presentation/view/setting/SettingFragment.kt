package com.kdn.presentation.view.setting

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.kdn.presentation.R
import com.kdn.presentation.databinding.FragmentSettingBinding
import com.kdn.presentation.view.cameramain.CameraFragment
import com.kdn.presentation.viewmodel.MainViewModel

class SettingFragment : Fragment() {
    private val viewModel by activityViewModels<MainViewModel>()

    private lateinit var binding: FragmentSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        binding.fragment = this


        checkFlashLightState()


        return binding.root
    }

    private fun checkFlashLightState(){
        if (viewModel.cameraFlashLight.value!!) binding.flashlightState.text = "켜짐"
        else binding.flashlightState.text = "꺼짐"
    }


    fun cameraFlashLightBtn(view: View){
        if (viewModel.cameraFlashLight.value!!) viewModel.setCameraFlashLight(false)
        else viewModel.setCameraFlashLight(true)
        checkFlashLightState()
    }

    fun locationBtn(view: View){
        this.findNavController().navigate(R.id.action_cameraAutoFragment_to_locationFragment)
    }

    //뒤로가기 클릭
    fun backBtn(view: View){
        view.findNavController().popBackStack()
    }
}