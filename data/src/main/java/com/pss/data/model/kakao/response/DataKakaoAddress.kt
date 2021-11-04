package com.pss.data.model.kakao.response

import com.pss.data.model.kakao.response.items.DataDocument
import com.pss.data.model.kakao.response.items.DataMeta

data class DataKakaoAddress(
    val documents: List<DataDocument>,
    val meta: DataMeta
)