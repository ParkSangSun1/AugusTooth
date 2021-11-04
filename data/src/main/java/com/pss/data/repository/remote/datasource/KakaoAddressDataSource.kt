package com.pss.data.repository.remote.datasource

import com.pss.data.model.kakao.response.DataKakaoAddress
import com.pss.data.model.kakao.response.items.DataDocument
import com.pss.domain.model.kakao.response.DomainKakaoAddress
import retrofit2.Response

interface KakaoAddressDataSource {
    suspend fun searchAddress(Authorization : String, analyze_type: String, page: Int, size:Int, query : String) : Response<DataKakaoAddress>
}