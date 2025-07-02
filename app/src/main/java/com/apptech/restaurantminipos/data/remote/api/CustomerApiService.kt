package com.apptech.restaurantminipos.data.remote.api

import com.apptech.restaurantminipos.data.dto.CustomerDto
import com.apptech.restaurantminipos.data.remote.response.MessageResponse
import com.apptech.restaurantminipos.domain.model.Customer
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CustomerApiService {

    @GET("customer")
    suspend fun getAll() : Response<List<Customer>>

    @GET("customer/get-by-code/{code}")
    suspend fun getByCode(@Path("code") code: Int) : Response<Customer>

    @POST("customer/add")
    suspend fun add(@Body customerDto: CustomerDto) : Response<MessageResponse>

    @PUT("customer/edit/{code}")
    suspend fun edit(@Path("code") code: Int, @Body customerDto: CustomerDto) : Response<MessageResponse>

    @DELETE("customer/delete/{code}")
    suspend fun delete(@Path("code") code: Int) : Response<MessageResponse>
}