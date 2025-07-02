package com.apptech.restaurantminipos.data.remote.api

import com.apptech.restaurantminipos.data.dto.CartDto
import com.apptech.restaurantminipos.data.remote.response.MessageResponse
import com.apptech.restaurantminipos.domain.model.Cart
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.math.BigDecimal

interface CartApiService {

    @GET("cart")
    suspend fun getAll() : Response<List<Cart>>

    @POST("cart/add")
    suspend fun add(@Body cartDto: CartDto) : Response<MessageResponse>

    @DELETE("cart/delete/{code}")
    suspend fun delete(@Path("code") code: Int) : Response<MessageResponse>

    @GET("cart/get-total-price")
    suspend fun getTotalPrice() : Response<BigDecimal>
}