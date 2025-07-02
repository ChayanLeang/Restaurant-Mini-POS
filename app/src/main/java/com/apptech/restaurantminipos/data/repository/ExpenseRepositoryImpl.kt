package com.apptech.restaurantminipos.data.repository

import com.apptech.restaurantminipos.data.dto.ExpenseDto
import com.apptech.restaurantminipos.data.remote.api.ExpenseApiService
import com.apptech.restaurantminipos.data.remote.response.MessageResponse
import com.apptech.restaurantminipos.domain.model.Expense
import com.apptech.restaurantminipos.domain.repository.ExpenseRepository
import com.apptech.restaurantminipos.util.Resource
import com.google.gson.Gson
import okio.IOException
import java.math.BigDecimal
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(private val expenseApiService: ExpenseApiService) :
                                                                                ExpenseRepository {
    override suspend fun getAll(): Resource<List<Expense>> {
        return try {
            val response = expenseApiService.getAll()
            if(response.isSuccessful){
                return Resource.Success(response.body() ?: emptyList())
            }
            Resource.Failure()
        }catch (_ : IOException){
            Resource.Failure("Please check your network")
        }catch (e : Exception){
            Resource.Failure(e.message ?: "Unknown error")
        }
    }

    override suspend fun getByCode(code: Int): Resource<Expense> {
        return try {
            val response = expenseApiService.getByCode(code)
            if(response.isSuccessful){
                return Resource.Success(response.body())
            }
            Resource.Failure(response.errorBody()?.string())
        }catch (_ : IOException){
            Resource.Failure("Please check your network")
        }catch (e : Exception){
            Resource.Failure(e.message ?: "Unknown error")
        }
    }

    override suspend fun add(expenseDto: ExpenseDto): Resource<String> {
        return try{
            val response = expenseApiService.add(expenseDto)
            if(response.isSuccessful){
                return Resource.Success(response.body()?.message)
            }
            val errorBody = response.errorBody()?.string().toString()
            val error = Gson().fromJson(errorBody, MessageResponse::class.java)
            Resource.Failure(error.message)
        }catch (_ : IOException){
            Resource.Failure("Please check your network")
        }catch (e : Exception){
            Resource.Failure(e.message ?: "Unknown Error")
        }
    }

    override suspend fun edit(code: Int,expenseDto: ExpenseDto): Resource<String> {
        return try{
            val response = expenseApiService.edit(code,expenseDto)
            if(response.isSuccessful){
                return Resource.Success(response.body()?.message)
            }
            val errorBody = response.errorBody()?.string().toString()
            val error = Gson().fromJson(errorBody, MessageResponse::class.java)
            Resource.Failure(error.message)
        }catch (_ : IOException){
            Resource.Failure("Please check your network")
        }catch (e : Exception){
            Resource.Failure(e.message ?: "Unknown Error")
        }
    }

    override suspend fun delete(code: Int): Resource<String> {
        return try {
            val response = expenseApiService.delete(code)
            if(response.isSuccessful){
                return Resource.Success(response.body()?.message)
            }
            Resource.Failure(response.errorBody()?.string())
        }catch (_ : IOException) {
            Resource.Failure("Please check your network")
        }catch (e : Exception){
            Resource.Failure(e.message ?: "Unknown error")
        }
    }

    override suspend fun getTotalExpense(): BigDecimal {
        return try {
            val response = expenseApiService.getTotalExpense()
            if(response.isSuccessful){
                return response.body() ?: BigDecimal.ZERO
            }
            BigDecimal.ZERO
        }catch (_ : Exception){
            BigDecimal.ZERO
        }
    }
}