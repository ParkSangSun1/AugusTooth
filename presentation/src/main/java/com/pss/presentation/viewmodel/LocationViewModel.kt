package com.pss.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pss.domain.model.kakao.response.DomainKakaoAddress
import com.pss.domain.usecase.SearchAddressUseCase
import com.pss.presentation.base.BaseViewModel
import com.pss.presentation.widget.utils.DataStore
import com.pss.presentation.widget.utils.DataStore.DEFAULT_LOCATION
import com.pss.presentation.widget.utils.DataStoreModule
import com.pss.presentation.widget.utils.Event
import com.pss.presentation.widget.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val addressUseCase: SearchAddressUseCase,
    private val dataStore: DataStoreModule
) : BaseViewModel() {

    //kakao address api로 위치를 검색한 결과 값
    val searchAddressResponse: LiveData<DomainKakaoAddress?> get() = _searchAddressResponse
    private val _searchAddressResponse = SingleLiveEvent<DomainKakaoAddress?>()

    //kakao address api로 위치를 검색한 성공여부
    val searchAddressResult: LiveData<Boolean> get() = _searchAddressResult
    private val _searchAddressResult =  SingleLiveEvent<Boolean>()

    //Gps로 위치를 검색할때
    val searchGpsAddressResponse: LiveData<String> get() = _searchGpsAddressResponse
    private val _searchGpsAddressResponse =  SingleLiveEvent<String>()


    suspend fun readLocationInDataStore() : String {
        var location : String = DataStore.DEFAULT_LOCATION
        viewModelScope.launch {
            location =  dataStore.readLocation.first()
        }
        return location
    }

    fun setSearchGpsAddressResponse(set : String) = _searchGpsAddressResponse.postValue(set)

    fun saveLocationInDataStore(location : String) = viewModelScope.launch {
        dataStore.setLocation(location)
    }

    fun searchAddress(
        Authorization: String,
        analyze_type: String,
        page: Int,
        size: Int,
        query: String
    ) {
        Log.d("TAG","로그 searchAddress")
        viewModelScope.launch {
            addressUseCase.execute(Authorization, analyze_type, page, size, query)
                .let { response ->
                    try {
                        Log.d("TAG","viewModel addressUseCase success")
                        _searchAddressResponse.value = response
                        _searchAddressResult.postValue(true)
                    } catch (e: Exception) {
                        Log.d("TAG","viewModel addressUseCase error")
                        _searchAddressResult.postValue(false)
                        viewEvent("ERROR")
                    }
                }
        }
    }
}