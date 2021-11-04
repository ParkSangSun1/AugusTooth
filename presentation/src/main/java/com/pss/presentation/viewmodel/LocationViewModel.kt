package com.pss.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pss.domain.model.kakao.response.DomainKakaoAddress
import com.pss.domain.usecase.SearchAddressUseCase
import com.pss.presentation.base.BaseViewModel
import com.pss.presentation.widget.utils.DataStore.DEFAULT_LOCATION
import com.pss.presentation.widget.utils.DataStoreModule
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

    val searchAddressResponse: LiveData<DomainKakaoAddress> get() = _searchAddressResponse
    private val _searchAddressResponse = MutableLiveData<DomainKakaoAddress>()

    //사용자가 최종적으로 선택한(Dialog에서) 위치
    val userChoiceLocation: LiveData<String> get() = _userChoiceLocation
    private val _userChoiceLocation = MutableLiveData<String>()


    fun setUserChoiceLocation(set: String) {
        _userChoiceLocation.value = set
    }

    fun saveLocationInDataStore() = viewModelScope.launch {
        dataStore.setLocation(_userChoiceLocation.value.toString())
    }

    fun setViewEventNull() = viewEvent("NULL")

    fun readLocationInDataStore() : String {
        var location : String = DEFAULT_LOCATION
        viewModelScope.launch {
           location =  dataStore.readLocation.first()
        }
        return location
    }

    fun searchAddress(
        Authorization: String,
        analyze_type: String,
        page: Int,
        size: Int,
        query: String
    ) {
        viewModelScope.launch {
            addressUseCase.execute(Authorization, analyze_type, page, size, query)
                .let { response ->
                    try {
                        _searchAddressResponse.value = response
                        viewEvent("SUCCESS")
                    } catch (e: Exception) {
                        viewEvent("ERROR")
                    }
                }
        }
    }
}