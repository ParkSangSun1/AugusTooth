package com.pss.data.model.kakao.response

import com.pss.data.model.kakao.response.item.Document
import com.pss.data.model.kakao.response.item.Meta

data class SearchAddress(
    val documents: List<Document>,
    val meta: Meta
)