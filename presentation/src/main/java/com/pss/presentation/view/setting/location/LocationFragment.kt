package com.pss.presentation.view.setting.location

import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.pss.presentation.R
import com.pss.presentation.base.BaseFragment
import com.pss.presentation.databinding.FragmentLocationBinding
import com.pss.presentation.viewmodel.LocationViewModel
import com.pss.presentation.widget.utils.ApiUrl.KEY
import android.os.Bundle
import androidx.lifecycle.Observer
import com.pss.presentation.widget.utils.DataStore.DEFAULT_LOCATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LocationFragment : BaseFragment<FragmentLocationBinding>(R.layout.fragment_location) {
    private val viewModel by activityViewModels<LocationViewModel>()

    //뒤로가기 클릭
    fun backBtn(view: View) {
        view.findNavController().popBackStack()
    }

    override fun init() {
        binding.fragment = this
        //initViewModel()
        //initText()
        observeViewModel()
    }

    private fun initViewModel() {
        viewModel.setViewEventNull()
        viewModel.setSearchAddressResponse(null)
    }

/*    private fun initText(){
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                if (viewModel.readLocationInDataStore() != DEFAULT_LOCATION)
                    binding.query.setText(viewModel.readLocationInDataStore())
            }
        }
    }*/

    private fun observeViewModel() {
        viewModel.viewEvent.observe(requireActivity(), {
            it.getContentIfNotHandled()?.let { event ->
                Log.d("TAG", "Event : $event")
                when (event) {
                    "SUCCESS" -> {
                        Log.d("TAG", "SUCCESS")
                        this@LocationFragment.findNavController().navigate(
                            LocationFragmentDirections.actionLocationFragmentToLocationSelectFragment(
                                "location"
                            )
                        )
                        //startLocationSelectFragment()
                        //viewModel.setViewEventNull()
                    }
                    "ERROR" -> shortShowToast("오류가 발생했습니다")
                }
            }
        })


 /*       viewModel.userChoiceLocation.observe(requireActivity(), Observer {
            Log.d("TAG", "사용자가 선택한 위치 : $it")
            viewModel.saveLocationInDataStore()
        })*/
    }

/*    private fun startLocationSelectFragment() {
        Log.d("TAG", "LocationSelectFramgent 함수1")
        if (viewModel.searchAddressResponse.value?.meta?.pageable_count != 0) {
            val location =
                "${viewModel.searchAddressResponse.value!!.documents[0].address.region_1depth_name} ${viewModel.searchAddressResponse.value!!.documents[0].address.region_2depth_name} ${viewModel.searchAddressResponse.value!!.documents[0].address.region_3depth_name}"
            Log.d("TAG", "LocationSelectFramgent 함수2")
            Log.d("TAG", "LocationSelectFramgent location : $location")
            this.findNavController().navigate(
                LocationFragmentDirections.actionLocationFragmentToLocationSelectFragment(location)
            )

        } else shortShowToast("검색한 주소의 값이 없습니다")
    }*/

    fun clickAddressSearchBtn(view: View) {
        if (!TextUtils.isEmpty(binding.query.text.toString())) viewModel.searchAddress(
            KEY,
            "similar",
            1,
            10,
            binding.query.text.toString()
        ) else shortShowToast("주소를 입력해 주세요")
  /*      this@LocationFragment.findNavController().navigate(
            LocationFragmentDirections.actionLocationFragmentToLocationSelectFragment(
                "location"
            )
        )*/
//        this.findNavController().navigate(LocationFragmentDirections.actionLocationFragmentToLocationSelectFragment("location"))

    }
}