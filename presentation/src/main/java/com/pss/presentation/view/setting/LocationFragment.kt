package com.pss.presentation.view.setting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.pss.presentation.R
import com.pss.presentation.base.BaseFragment
import com.pss.presentation.databinding.FragmentLocationBinding
import com.pss.presentation.viewmodel.CameraMainViewModel
import com.pss.presentation.viewmodel.LocationViewModel
import com.pss.presentation.widget.utils.ApiUrl.KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationFragment : BaseFragment<FragmentLocationBinding>(R.layout.fragment_location) {
    private val viewModel by activityViewModels<LocationViewModel>()

    //뒤로가기 클릭
    fun backBtn(view: View) {
        view.findNavController().popBackStack()
    }

    override fun init() {
        binding.fragment = this
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.viewEvent.observe(requireActivity(), {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    "SUCCESS" -> Log.d("TAG", "${viewModel.searchAddressResponse.value}")
                    "ERROR" -> Toast.makeText(requireContext(), "오류가 발생했습니다", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun clickAddressSearchBtn(view: View) = viewModel.searchAddress(KEY, "similar", 1, 10, binding.query.text.toString())

}