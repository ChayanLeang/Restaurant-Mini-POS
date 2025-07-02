package com.apptech.restaurantminipos.data.remote.api

import com.apptech.restaurantminipos.data.dto.OrderTypeDto
import com.apptech.restaurantminipos.data.remote.response.MessageResponse
import com.apptech.restaurantminipos.domain.model.OrderType
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrderTypeApiService {

    @GET("order-type")
    suspend fun getAll() : Response<List<OrderType>>

    @GET("order-type/get-by-code/{code}")
    suspend fun getByCode(@Path("code") code: Int) : Response<OrderType>

    @POST("order-type/add")
    suspend fun add(@Body orderTypeDto: OrderTypeDto) : Response<MessageResponse>

    @PUT("order-type/edit/{code}")
    suspend fun edit(@Path("code") code: Int,@Body orderTypeDto: OrderTypeDto) : Response<MessageResponse>

    @DELETE("order-type/delete/{code}")
    suspend fun delete(@Path("code") code: Int) : Response<MessageResponse>
}