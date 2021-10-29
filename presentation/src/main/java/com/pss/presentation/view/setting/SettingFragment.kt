package com.pss.presentation.view.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.pss.presentation.R
import com.pss.presentation.databinding.FragmentSettingBinding
import com.pss.presentation.viewmodel.CameraMainViewModel

class SettingFragment : Fragment() {
    private val viewModel by activityViewModels<CameraMainViewModel>()

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

    private fun checkFlashLightState() {
        if (viewModel.cameraFlashLight.value!!) binding.flashlightState.text = "켜짐"
        else binding.flashlightState.text = "꺼짐"
    }


    fun cameraFlashLightBtn(view: View) {
        if (viewModel.cameraFlashLight.value!!) viewModel.setCameraFlashLight(false)
        else viewModel.setCameraFlashLight(true)
        checkFlashLightState()
    }

    fun locationBtn(view: View) {
        Toast.makeText(requireContext(), "해당 기능은 사용할 수 없습니다", Toast.LENGTH_SHORT).show()
        // this.findNavController().navigate(R.id.action_cameraAutoFragment_to_locationFragment)
    }

    //뒤로가기 클릭
    fun backBtn(view: View) {
        view.findNavController().popBackStack()
    }
}