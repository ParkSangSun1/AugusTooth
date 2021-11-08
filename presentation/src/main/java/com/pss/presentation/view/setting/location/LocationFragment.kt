package com.pss.presentation.view.setting.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import com.pss.presentation.widget.utils.ApiUrl
import com.pss.presentation.widget.utils.DataStore.DEFAULT_LOCATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*


class LocationFragment : BaseFragment<FragmentLocationBinding>(R.layout.fragment_location) {
    private val viewModel by activityViewModels<LocationViewModel>()

    //뒤로가기 클릭
    fun backBtn(view: View) {
        view.findNavController().popBackStack()
    }

    override fun init() {
        binding.fragment = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    fun searchGpsAddressClick(view: View) = shortShowToast("해당 기능은 아직 비활성화 상태입니다")

    private fun observeViewModel() {
        viewModel.searchAddressResult.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                var location = "주소 검색에 실패했습니다"
                if (viewModel.searchAddressResponse.value?.meta?.pageable_count != 0) viewModel.searchAddressResponse.value!!.documents[0].address.apply {
                    location = "$region_1depth_name $region_2depth_name $region_3depth_name"
                    this@LocationFragment.findNavController().navigate(
                        LocationFragmentDirections.actionLocationFragmentToLocationSelectFragment(
                            location
                        )
                    )
                } else shortShowToast("존재하지 않는 주소입니다")
                return@observe
            } else shortShowToast("주소 검색에 실패했습니다")
        }
    }

    fun clickAddressSearchBtn(view: View) {
        if (!TextUtils.isEmpty(binding.query.text.toString()))
            searchLocation()
        else shortShowToast("주소를 입력해 주세요")
    }

    private fun searchLocation() {
        viewModel.searchAddress(
            ApiUrl.KEY,
            "similar",
            1,
            10,
            binding.query.text.toString()
        )
    }
}