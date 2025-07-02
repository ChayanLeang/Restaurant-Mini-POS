package com.apptech.restaurantminipos.data.remote.api

import com.apptech.restaurantminipos.data.dto.ExpenseDto
import com.apptech.restaurantminipos.data.remote.response.MessageResponse
import com.apptech.restaurantminipos.domain.model.Expense
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.math.BigDecimal

interface ExpenseApiService {

    @GET("expense")
    suspend fun getAll() : Response<List<Expense>>

    @GET("expense/get-by-code/{code}")
    suspend fun getByCode(@Path("code") code: Int) : Response<Expense>

    @POST("expense/add")
    suspend fun add(@Body expenseDto: ExpenseDto) : Response<MessageResponse>

    @PUT("expense/edit/{code}")
    suspend fun edit(@Path("code") code: Int,@Body expenseDto: ExpenseDto) : Response<MessageResponse>

    @DELETE("expense/delete/{code}")
    suspend fun delete(@Path("code") code: Int) : Response<MessageResponse>

    @GET("expense/get-total-expense")
    suspend fun getTotalExpense() : Response<BigDecimal>
}