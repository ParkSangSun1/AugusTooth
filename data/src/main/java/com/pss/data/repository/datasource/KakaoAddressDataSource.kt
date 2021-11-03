package com.pss.data.repository.datasource

import com.pss.data.model.kakao.response.SearchAddress
import retrofit2.Response

interface KakaoAddressDataSource {
    suspend fun searchAddress(Authorization : String, analyze_type: String, page: Int, size:Int, query : String) : List<SearchAddress>

}