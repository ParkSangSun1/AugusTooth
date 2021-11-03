package com.pss.domain.repository

import com.pss.data.model.kakao.response.SearchAddress

interface KakaoAddressRepository {
    suspend fun searchAddress(Authorization : String, analyze_type: String, page: Int, size:Int, query : String) : List<SearchAddress>
}