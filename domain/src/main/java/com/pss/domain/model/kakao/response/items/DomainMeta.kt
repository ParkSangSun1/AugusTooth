package com.pss.domain.model.kakao.response.items

data class DomainMeta(
    val is_end: Boolean,
    val pageable_count: Int,
    val total_count: Int
)