package com.kdn.data.api

import com.kdn.data.model.hospitalsearch.HospitalSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HospitalSearchService {
    @GET("getHsptlMdcncListInfoInqire")
    suspend fun getHospitalSearch(
        @Query("Q0") Q0: String,
        @Query("Q1") Q1: String,
        @Query("QZ") QZ: String,
        @Query("ORD") ORD: String,
        @Query("pageNo") pageNo: String,
        @Query("numOfRows") numOfRows: String,
        @Query("serviceKey") serviceKey: String
    ) : Response<HospitalSearchResponse>
}