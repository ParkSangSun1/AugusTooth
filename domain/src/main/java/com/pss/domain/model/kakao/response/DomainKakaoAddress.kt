package com.pss.domain.model.kakao.response

import com.pss.domain.model.kakao.response.items.DomainDocument
import com.pss.domain.model.kakao.response.items.DomainMeta

data class DomainKakaoAddress(
    val documents: List<DomainDocument>,
    val meta: DomainMeta
)