package com.pss.data.model.kakao.response.items

data class DataDocument(
    val address: DataAddress,
    val address_name: String,
    val address_type: String,
    val road_address: Any?,
    val x: String,
    val y: String
)