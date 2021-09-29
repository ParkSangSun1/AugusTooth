package com.kdn.data.model.hospitalsearch

import javax.xml.bind.annotation.*

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
data class HospitalSearchResponse(
    @field:XmlElementWrapper(name = "body")
    @field:XmlElement(name = "items")
    val body: List<Items>? = arrayListOf()
)
