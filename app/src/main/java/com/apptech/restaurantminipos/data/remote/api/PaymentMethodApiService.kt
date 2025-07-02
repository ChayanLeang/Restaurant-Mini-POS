package com.apptech.restaurantminipos.data.remote.api

import com.apptech.restaurantminipos.data.dto.PaymentMethodDto
import com.apptech.restaurantminipos.data.remote.response.MessageResponse
import com.apptech.restaurantminipos.domain.model.PaymentMethod
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PaymentMethodApiService {

    @GET("payment-method")
    suspend fun getAll() : Response<List<PaymentMethod>>

    @GET("payment-method/get-by-code/{code}")
    suspend fun getByCode(@Path("code") code: Int) : Response<PaymentMethod>

    @POST("payment-method/add")
    suspend fun add(@Body paymentMethodDto: PaymentMethodDto) : Response<MessageResponse>

    @PUT("payment-method/edit/{code}")
    suspend fun edit(@Path("code") code: Int, @Body paymentMethodDto: PaymentMethodDto) :
                                                                Response<MessageResponse>

    @DELETE("payment-method/delete/{code}")
    suspend fun delete(@Path("code") code: Int) : Response<MessageResponse>
}