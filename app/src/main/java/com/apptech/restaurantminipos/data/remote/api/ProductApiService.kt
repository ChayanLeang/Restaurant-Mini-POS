package com.apptech.restaurantminipos.data.remote.api

import com.apptech.restaurantminipos.data.remote.response.MessageResponse
import com.apptech.restaurantminipos.domain.model.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProductApiService {

    @GET("product")
    suspend fun getAll() : Response<List<Product>>

    @GET("product/get-by-code/{code}")
    suspend fun getByCode(@Path("code") code: Int) : Response<Product>

    @Multipart
    @POST("product/add")
    suspend fun add(@Part image: MultipartBody.Part,@Part("code") code: RequestBody,
                    @Part("name") name: RequestBody, @Part("categoryCode") categoryCode: RequestBody,
                    @Part("sellPrice") sellPrice: RequestBody) : Response<MessageResponse>

    @Multipart
    @PUT("product/edit/{code}")
    suspend fun edit(@Path("code") code: Int, @Part("image") image: MultipartBody.Part,
                     @Part("name") name: RequestBody, @Part("categoryCode") categoryCode: RequestBody,
                     @Part("sellPrice") sellPrice: RequestBody) : Response<MessageResponse>

    @DELETE("product/delete/{code}")
    suspend fun delete(@Path("code") code: Int) : Response<MessageResponse>
}