package com.pss.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pss.domain.model.kakao.response.DomainKakaoAddress
import com.pss.domain.usecase.SearchAddressUseCase
import com.pss.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val addressUseCase: SearchAddressUseCase
) : BaseViewModel() {

    companion object{
        const val SEARCH_ADDRESS_RESPONSE = 0
    }

    val searchAddressResponse : LiveData<Response<DomainKakaoAddress>> get() = _searchAddressResponse
    private val _searchAddressResponse = MutableLiveData<Response<DomainKakaoAddress>>()

    fun searchAddress(
        Authorization: String,
        analyze_type: String,
        page: Int,
        size: Int,
        query: String
    ) {
        Log.d("TAG","Response : 들어옴1")

        viewModelScope.launch {
            Log.d("TAG","Response : 들어옴2")
         /*   try {
                Log.d("TAG","Response : 들어옴3")
*/
                addressUseCase.execute(Authorization, analyze_type, page, size, query)
                    .let { response ->
                        Log.d("TAG","Response : $response")
                     /*   if (response.){
                            _searchAddressResponse.value = response
                            viewEvent("SUCCESS")
                        }
                        else
                            viewEvent("ERROR")*/
                    }
         /*   } catch (e: Exception) {
                viewEvent("ERROR")
            }*/
        }
    }
}