package com.pss.presentation.view.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.pss.presentation.R
import com.pss.presentation.base.BaseFragment
import com.pss.presentation.databinding.FragmentLocationBinding

class LocationFragment : BaseFragment<FragmentLocationBinding>(R.layout.fragment_location) {

    //뒤로가기 클릭
    fun backBtn(view: View){
        view.findNavController().popBackStack()
    }

    override fun init() {
        binding.fragment = this
    }
}