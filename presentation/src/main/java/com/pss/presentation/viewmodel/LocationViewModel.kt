package com.pss.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pss.domain.model.kakao.response.SearchAddress
import com.pss.domain.usecase.SearchAddressUseCase
import com.pss.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val addressUseCase: SearchAddressUseCase
) : BaseViewModel() {

    companion object{
        const val SEARCH_ADDRESS_RESPONSE = 0
    }

    val searchAddressResponse : LiveData<Response<SearchAddress>> get() = _searchAddressResponse
    private val _searchAddressResponse = MutableLiveData<Response<SearchAddress>>()

    fun searchAddress(
        Authorization: String,
        analyze_type: String,
        page: Int,
        size: Int,
        query: String
    ) {
        viewModelScope.launch {
            try {
                addressUseCase.execute(Authorization, analyze_type, page, size, query)
                    .let { response ->
                        if (response.isSuccessful){
                            _searchAddressResponse.value = response
                            viewEvent("SUCCESS")
                        }
                        else
                            viewEvent("ERROR")
                    }
            } catch (e: Exception) {
                viewEvent("ERROR")
            }
        }
    }
}