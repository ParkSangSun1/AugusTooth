package com.pss.domain.model.kakao.response.items

data class DomainDocument(
    val address: DomainAddress,
    val address_name: String,
    val address_type: String,
    val road_address: Any?,
    val x: String,
    val y: String
)