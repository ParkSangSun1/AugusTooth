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
        searchLocation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.viewEvent.observe(requireActivity(), {
            it.peekContent().let { event ->
                Log.d("TAG", "Event : $event")
                when (event) {
                    "SUCCESS" -> {
                        initText()
                    }
                    "ERROR" -> shortShowToast("오류가 발생했습니다")
                    "SAVE_SUCCESS" -> this@LocationSelectFragment.findNavController().popBackStack()
                }
            }
        })
    }

    private fun searchLocation() {
        viewModel.searchAddress(
            ApiUrl.KEY,
            "similar",
            1,
            10,
            args.location
        )
    }

    private fun initText() {
        if (viewModel.searchAddressResponse.value?.meta?.pageable_count != 0) viewModel.searchAddressResponse.value!!.documents[0].address.apply {
            binding.userLocation.text =
                "$region_1depth_name $region_2depth_name $region_3depth_name"
        }
    }

    fun clickChoiceBtn(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.saveLocationInDataStore(binding.userLocation.text.toString())
        }
    }

    fun clickNotChoiceBtn(view: View) {
        this@LocationSelectFragment.findNavController().popBackStack()
    }
}