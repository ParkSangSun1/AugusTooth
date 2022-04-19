package com.pss.presentation.view.setting.location

import android.content.Context
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
import android.os.Bundle
import com.pss.presentation.widget.utils.ApiUrl
import com.pss.presentation.widget.utils.DataStore.DEFAULT_LOCATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.lifecycle.lifecycleScope
import com.pss.presentation.widget.utils.LocationHelper
import java.io.IOException
import java.util.*
import java.lang.IllegalArgumentException


class LocationFragment : BaseFragment<FragmentLocationBinding>(R.layout.fragment_location) {
    private val viewModel by activityViewModels<LocationViewModel>()


    override fun init() {
        binding.fragment = this
        observeViewModel()
        initReadDataStore()
    }

    override fun onResume() {
        super.onResume()
        initReadDataStore()
    }

    //저장된 주소 가져오기
    private fun initReadDataStore() = CoroutineScope(Dispatchers.Main).launch {
        viewModel.readLocationInDataStore().apply {
            Log.d("TAG", "readLocationInDataSTore Value : $this")
            if (this != DEFAULT_LOCATION) binding.query.setText(this)
        }
    }

    //GPS 사용 위치 검색 클릭 시
    fun searchGpsAddressClick(view: View) {
        getGpsLocation()
    }

    //뒤로가기 클릭
    fun backBtn(view: View) {
        view.findNavController().popBackStack()
    }

    //주소 검색 버튼 클릭 시
    fun addressSearchBtnClick(view: View) {
        if (!TextUtils.isEmpty(binding.query.text.toString()))
            searchLocation()
        else shortShowToast("주소를 입력해 주세요")
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
            searchGpsEnd()
        }
    }

    //GPS 검색 끝
    private fun searchGpsEnd(){
        binding.gpsBtn.isEnabled = true
        binding.progressBar.visibility = View.INVISIBLE
        binding.backBtn.isEnabled = true
    }

    //GPS 검색 시작
    private fun searchGpsStart(){
        binding.gpsBtn.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE
        binding.backBtn.isEnabled = false
    }

    //GPS 사용
    private fun getGpsLocation() {
        val mLocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //GPS 연결되었는지 확인
        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            searchGpsStart()

            lifecycleScope.launch(Dispatchers.Main) {
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
                                var mLocation = ""
                                val locationArray = getCurrentAddress(
                                    location.latitude,
                                    location.longitude
                                ).split(" ")
                                for (num in 1..3) {
                                    mLocation = mLocation.plus("${locationArray[num]} ")
                                }
                                viewModel.setSearchGpsAddressResponse(
                                    mLocation
                                )

                            } catch (e: Exception) {
                                searchGpsEnd()
                                viewModel.setSearchGpsAddressResponse("ERROR")
                            }
                        }
                    })
            }
        }else shortShowToast("GPS를 켜주세요")
    }

    //주소 검색
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

    //주소 검색 api 호출
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