package com.kdn.presentation.view

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
        /*  if (allPermissionsGranted()) {
              startCamera()
          } else {
              ActivityCompat.requestPermissions(
                  requireActivity(),
                  CameraFragment.REQUIRED_PERMISSIONS,
                  CameraFragment.REQUEST_CODE_PERMISSIONS
              )
          }*/

        checkFlashLightState()


        return binding.root
    }

    private fun checkFlashLightState(){
        if (viewModel.cameraFlashLight.value!!) binding.flashlightState.text = "켜짐"
        else binding.flashlightState.text = "꺼짐"
    }

    //카메라 권한 승인 클릭
    fun cameraAuthBtn(view: View) {
        if (allPermissionsGranted()) {
            Toast.makeText(
                requireContext(),
                "이미 권한을 승인했습니다",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                CameraFragment.REQUIRED_PERMISSIONS,
                CameraFragment.REQUEST_CODE_PERMISSIONS
            )
        }
    }

    fun cameraFlashLightBtn(view: View){
        if (viewModel.cameraFlashLight.value!!) viewModel.setCameraFlashLight(false)
        else viewModel.setCameraFlashLight(true)
        checkFlashLightState()
    }

    //뒤로가기 클릭
    fun backBtn(view: View){
        view.findNavController().popBackStack()
    }

    //권한승인 요청 or 권한확인
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CameraFragment.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                this.findNavController().popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    "권한을 반드시 승인해야 합니다",
                    Toast.LENGTH_SHORT
                ).show()

            }
            this.findNavController().popBackStack()
        }
    }

    private fun allPermissionsGranted() = CameraFragment.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

}