package com.apptech.restaurantminipos.data.remote.api

import com.apptech.restaurantminipos.data.dto.CategoryDto
import com.apptech.restaurantminipos.data.remote.response.MessageResponse
import com.apptech.restaurantminipos.domain.model.Category
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CategoryApiService {

    @GET("category")
    suspend fun getAll() : Response<List<Category>>

    @GET("category/get-by-code/{code}")
    suspend fun getByCode(@Path("code") code: Int) : Response<Category>

    @POST("category/add")
    suspend fun add(@Body categoryDto: CategoryDto) : Response<MessageResponse>

    @PUT("category/edit/{code}")
    suspend fun edit(@Path("code") code: Int,@Body categoryDto: CategoryDto) : Response<MessageResponse>

    @DELETE("category/delete/{code}")
    suspend fun delete(@Path("code") code: Int) : Response<MessageResponse>
}