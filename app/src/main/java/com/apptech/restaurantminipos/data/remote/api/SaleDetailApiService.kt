package com.apptech.restaurantminipos.data.remote.api

import com.apptech.restaurantminipos.domain.model.SaleDetail
import com.apptech.restaurantminipos.domain.model.Summary
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SaleDetailApiService {

    @GET("sale-detail/get-by-sale-code/{saleCode}")
    suspend fun getBySaleCode(@Path("saleCode") saleCode: Int) : Response<List<SaleDetail>>

    @GET("sale-detail/get-summary/{saleCode}")
    suspend fun getSummaryByCode(@Path("saleCode") saleCode: Int) : Response<Summary>
}