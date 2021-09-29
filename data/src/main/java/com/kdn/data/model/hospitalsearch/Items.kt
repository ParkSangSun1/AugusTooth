package com.kdn.data.model.hospitalsearch

import javax.xml.bind.annotation.*

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
data class Items(
    @field:XmlElementWrapper(name = "items")
    @field:XmlElement(name = "item")
    val items : List<Item>? = arrayListOf()
)