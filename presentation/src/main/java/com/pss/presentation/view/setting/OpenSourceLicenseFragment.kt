package com.pss.presentation.view.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.pss.presentation.R
import com.pss.presentation.base.BaseFragment
import com.pss.presentation.databinding.FragmentOpenSourceLicenseBinding

class OpenSourceLicenseFragment : BaseFragment<FragmentOpenSourceLicenseBinding>(R.layout.fragment_open_source_license) {

    override fun init() {
        binding.fragment = this
    }

    //뒤로가기 클릭
    fun backBtn(view: View) {
        view.findNavController().popBackStack()
    }
}