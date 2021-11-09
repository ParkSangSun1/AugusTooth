package com.pss.presentation.view.setting.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
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
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.pss.presentation.widget.utils.LocationHelper
import java.io.IOException
import java.util.*
import android.widget.Toast
import java.lang.IllegalArgumentException


class LocationFragment : BaseFragment<FragmentLocationBinding>(R.layout.fragment_location) {
    private val viewModel by activityViewModels<LocationViewModel>()
    var mLocationManager: LocationManager? = null
    var mLocationListener: LocationListener? = null
    var mContext = this

    //뒤로가기 클릭
    fun backBtn(view: View) {
        view.findNavController().popBackStack()
    }

    override fun init() {
        binding.fragment = this
        initReadDataStore()
    }

    private fun initReadDataStore() = CoroutineScope(Dispatchers.Main).launch {
        viewModel.readLocationInDataStore().apply {
            Log.d("TAG", "readLocationInDataSTore Value : $this")
            if (this != DEFAULT_LOCATION) binding.query.setText(this.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    fun searchGpsAddressClick(view: View) {
        getLocation()
    }

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

        viewModel.searchGpsAddressResponse.observe(viewLifecycleOwner) { response ->
            Log.d("TAG", "GPS RESPONSE : $response")
            if (response == "ERROR") shortShowToast("주소를 검색하는데 오류가 발생했습니다")
            else {
                binding.query.setText(response)
                viewModel.saveLocationInDataStore(response)
                shortShowToast("위치가 저장되었습니다")
            }
            binding.progressBar.isEnabled = true
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun getLocation() {
        binding.progressBar.visibility = View.VISIBLE
        binding.progressBar.isEnabled = false
        CoroutineScope(Dispatchers.Main).launch {
            LocationHelper().startListeningUserLocation(
                requireContext(),
                object : LocationHelper.MyLocationListener {
                    override fun onLocationChanged(location: Location) {
                        try {
                            Log.d(
                                "TAG",
                                "LOCATION : " + "현재 위치 - ${
                                    getCurrentAddress(
                                        location.latitude,
                                        location.longitude
                                    )
                                }" + location.latitude + "," + location.longitude
                            )
                            viewModel.setSearchGpsAddressResponse(
                                getCurrentAddress(
                                    location.latitude,
                                    location.longitude
                                )
                            )
                        } catch (e: Exception) {
                            viewModel.setSearchGpsAddressResponse("ERROR")
                        }
                    }
                })
        }

    }

    fun clickAddressSearchBtn(view: View) {
        if (!TextUtils.isEmpty(binding.query.text.toString()))
            searchLocation()
        else shortShowToast("주소를 입력해 주세요")
    }

    fun getCurrentAddress(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>? = try {
            geocoder.getFromLocation(
                latitude,
                longitude,
                7
            )
        } catch (ioException: IOException) {
            return "GPS를 키거나 또는 네트워크를 확인해 주세요"
        } catch (illegalArgumentException: IllegalArgumentException) {
            return "잘못된 GPS 좌표입니다. 주소 검색을 이용해 주세요"
        }
        if (addresses == null || addresses.isEmpty()) return "주소가 발견되지 않습니다. 다시 시도하거나 주소 검색을 이용해 주세요"
        val address = addresses[0]
        return address.getAddressLine(0).toString()
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