package com.apptech.restaurantminipos.data.remote.api

import com.apptech.restaurantminipos.data.dto.SaleDto
import com.apptech.restaurantminipos.data.remote.response.MessageResponse
import com.apptech.restaurantminipos.domain.model.Sale
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SaleApiService {

    @GET("sale")
    suspend fun getAll() : Response<List<Sale>>

    @POST("sale/add")
    suspend fun add(@Body saleDto: SaleDto) : Response<MessageResponse>
}