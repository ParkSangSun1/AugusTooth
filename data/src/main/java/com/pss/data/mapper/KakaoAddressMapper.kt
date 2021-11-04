package com.pss.data.mapper

import com.pss.data.mapper.KakaoAddressMapper.toDomain
import com.pss.data.model.kakao.response.DataKakaoAddress
import com.pss.data.model.kakao.response.items.DataAddress
import com.pss.data.model.kakao.response.items.DataDocument
import com.pss.data.model.kakao.response.items.DataMeta
import com.pss.domain.model.kakao.response.DomainKakaoAddress
import com.pss.domain.model.kakao.response.items.DomainAddress
import com.pss.domain.model.kakao.response.items.DomainDocument
import com.pss.domain.model.kakao.response.items.DomainMeta
import retrofit2.Response

object KakaoAddressMapper {
/*    fun mapperToDomainAddress(kakaoDocumentResponse: DataDocument): DomainDocument {
        return DomainDocument(
            address = kakaoDocumentResponse.address.toDomain(),
            address_name = kakaoDocumentResponse.address_name,
            address_type = kakaoDocumentResponse.address_type,
            road_address = kakaoDocumentResponse.road_address,
            x = kakaoDocumentResponse.x,
            y = kakaoDocumentResponse.y
        )
    }*/

/*    fun mapperToDomainAddress(response: DataKakaoAddress): DomainKakaoAddress {
        return DomainKakaoAddress(
            documents = response.documents.toDomain(),
            meta = response.meta.toDomain()
        )
    }*/

    fun mapperToDomainAddress(response: DataKakaoAddress): DomainKakaoAddress {
        return DomainKakaoAddress(
            documents = response.documents.toDomain(),
            meta = response.meta.toDomain()
        )
    }

    fun DataKakaoAddress.toDomain(): DomainKakaoAddress {
        return DomainKakaoAddress(
            this.documents.toDomain(),
            this.meta.toDomain(),
        )
    }

    fun DataMeta.toDomain(): DomainMeta {
        return DomainMeta(
            this.is_end,
            this.pageable_count,
            this.total_count
        )
    }

    fun List<DataDocument>.toDomain(): List<DomainDocument> {
        return this.map {
            DomainDocument(
                it.address.toDomain(),
                it.address_name,
                it.address_type,
                it.road_address,
                it.x,
                it.y
            )
        }
        /*  return DomainDocument(
              this.address.toDomain(),
              this.address_name,
              this.address_type,
              this.road_address,
              this.x,
              this.y
          )*/
    }

    private fun DataAddress.toDomain(): DomainAddress {
        return DomainAddress(
            this.address_name,
            this.b_code,
            this.h_code,
            this.main_address_no,
            this.mountain_yn,
            this.region_1depth_name,
            this.region_2depth_name,
            this.region_3depth_h_name,
            this.region_3depth_name,
            this.sub_address_no,
            this.x,
            this.y,
        )
    }
}