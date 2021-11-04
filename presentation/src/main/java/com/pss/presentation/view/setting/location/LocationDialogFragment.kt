package com.pss.presentation.view.setting.location

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.pss.presentation.R
import com.pss.presentation.base.BaseDialogFragment
import com.pss.presentation.databinding.FragmentLocationDialogBinding
import com.pss.presentation.viewmodel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint

class LocationDialogFragment :
    BaseDialogFragment<FragmentLocationDialogBinding>(R.layout.fragment_location_dialog) {
    private val viewModel by activityViewModels<LocationViewModel>()
    private var location = "검색된 주소가 없습니다"
    override fun init() {
        binding.dialog = this
      getArgValues()
        initText()
    }
    private fun getArgValues(){
        val mArgs = arguments
        location = mArgs!!.getString("location").toString()
        binding.userLocation.text = location
    }

    private fun initText() {
        binding.notChoice.text = "다시선택"
        binding.choice.text = "선택"
        binding.title.text = "사용자 위치 설정"
    }

    fun clickChoiceBtn(view: View) {
        viewModel.setUserChoiceLocation(location)
        dismiss()
    }

    fun clickNotChoiceBtn(view: View) {
        dismiss()
    }
}