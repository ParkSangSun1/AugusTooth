package com.pss.data.repository.datasourcelmpl

import com.pss.data.api.KakaoAddressApi
import com.pss.domain.model.kakao.response.SearchAddress
import com.pss.data.repository.datasource.KakaoAddressDataSource
import retrofit2.Response
import javax.inject.Inject

class KakaoAddressDataSourceImpl @Inject constructor(
    private val searchAddressApi: KakaoAddressApi,
) : KakaoAddressDataSource {
    override suspend fun searchAddress(
        Authorization: String,
        analyze_type: String,
        page: Int,
        size: Int,
        query: String
    ): Response<SearchAddress> = searchAddressApi.searchAddress(
        Authorization = Authorization,
        analyze_type = analyze_type,
        page = page,
        size = size,
        query = query
    )
}