package com.pss.data.model.kakao.response.item

data class Document(
    val address: AddressItem?,
    val address_name: String?,
    val address_type: String?,
    val road_address: RoadAddress?,
    val x: String?,
    val y: String?
)