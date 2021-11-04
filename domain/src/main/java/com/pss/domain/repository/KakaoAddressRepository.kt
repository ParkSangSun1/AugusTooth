package com.pss.domain.repository

import com.pss.domain.model.kakao.response.DomainKakaoAddress
import com.pss.domain.model.kakao.response.items.DomainDocument
import retrofit2.Response

interface KakaoAddressRepository {
    suspend fun searchAddress(Authorization : String, analyze_type: String, page: Int, size:Int, query : String) : DomainKakaoAddress
}