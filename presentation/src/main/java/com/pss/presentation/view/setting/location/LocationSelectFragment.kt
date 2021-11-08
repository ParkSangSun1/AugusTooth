package com.pss.presentation.view.setting.location

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pss.presentation.R
import com.pss.presentation.base.BaseFragment
import com.pss.presentation.databinding.FragmentLocationSelectBinding
import com.pss.presentation.view.imageanalysis.ImageAnalysisFragmentArgs
import com.pss.presentation.viewmodel.LocationViewModel
import com.pss.presentation.widget.utils.ApiUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LocationSelectFragment :
    BaseFragment<FragmentLocationSelectBinding>(R.layout.fragment_location_select) {
    private val args by navArgs<LocationSelectFragmentArgs>()
    private val viewModel by activityViewModels<LocationViewModel>()


    override fun init() {
        binding.fragment = this
        initText()
    }

    private fun initText() {
        binding.userLocation.text = args.location
    }

    fun clickChoiceBtn(view: View) {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.saveLocationInDataStore(binding.userLocation.text.toString())
            this@LocationSelectFragment.findNavController().popBackStack()
        }
    }

    fun clickNotChoiceBtn(view: View) {
        this@LocationSelectFragment.findNavController().popBackStack()
    }
}