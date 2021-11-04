package com.pss.data.repository.remote

import com.pss.data.mapper.KakaoAddressMapper
import com.pss.data.model.kakao.response.DataKakaoAddress
import com.pss.data.repository.remote.datasource.KakaoAddressDataSource
import com.pss.domain.model.kakao.response.DomainKakaoAddress
import com.pss.domain.model.kakao.response.items.DomainDocument
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
    ): DomainKakaoAddress {
        return KakaoAddressMapper.mapperToDomainAddress(kakaoAddressDataSource.searchAddress(Authorization, analyze_type, page, size, query).body()!!)

    }
}