package com.pss.domain.usecase

import com.pss.domain.repository.KakaoAddressRepository

class SearchAddressUseCase constructor(
    private val kakaoAddressRepository: KakaoAddressRepository
) {
    suspend fun execute(Authorization : String, analyze_type: String, page: Int, size:Int, query : String) = kakaoAddressRepository.searchAddress(Authorization, analyze_type, page, size, query)
}