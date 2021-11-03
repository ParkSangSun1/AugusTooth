package com.pss.data.repository

import com.pss.data.repository.datasource.KakaoAddressDataSource
import com.pss.domain.model.kakao.response.SearchAddress
import com.pss.domain.repository.KakaoAddressRepository
import retrofit2.Response
import javax.inject.Inject

class KakaoAddressRepositoryImpl @Inject constructor(
    private val kakaoAddressDataSource: KakaoAddressDataSource
) : KakaoAddressRepository {
    override suspend fun searchAddress(
        Authorization: String,
        analyze_type: String,
        page: Int,
        size: Int,
        query: String
    ): Response<SearchAddress>  = kakaoAddressDataSource.searchAddress(Authorization, analyze_type, page, size, query)
}