package com.pss.domain.repository

import com.pss.domain.model.kakao.response.SearchAddress
import retrofit2.Response

interface KakaoAddressRepository {
    suspend fun searchAddress(Authorization : String, analyze_type: String, page: Int, size:Int, query : String) : Response<SearchAddress>
}