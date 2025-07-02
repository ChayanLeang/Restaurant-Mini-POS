package com.apptech.restaurantminipos.data.repository

import com.apptech.restaurantminipos.data.dto.OrderTypeDto
import com.apptech.restaurantminipos.data.remote.api.OrderTypeApiService
import com.apptech.restaurantminipos.data.remote.response.MessageResponse
import com.apptech.restaurantminipos.domain.model.OrderType
import com.apptech.restaurantminipos.domain.repository.OrderTypeRepository
import com.apptech.restaurantminipos.util.Resource
import com.google.gson.Gson
import okio.IOException
import javax.inject.Inject

class OrderTypeRepositoryImpl @Inject constructor(private val orderTypeApiService: OrderTypeApiService) :
                                                                                    OrderTypeRepository {
    override suspend fun getAll(): Resource<List<OrderType>> {
        return try {
            val response = orderTypeApiService.getAll()
            if(response.isSuccessful){
                return Resource.Success(response.body() ?: emptyList())
            }
            Resource.Failure("")
        }catch (_ : IOException){
            Resource.Failure("Please check your network")
        }catch (e : Exception){
            Resource.Failure(e.message ?: "Unknown error")
        }
    }

    override suspend fun getByCode(code: Int): Resource<OrderType> {
        return try {
            val response = orderTypeApiService.getByCode(code)
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

    override suspend fun add(orderTypeDto: OrderTypeDto): Resource<String> {
        return try{
            val response = orderTypeApiService.add(orderTypeDto)
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

    override suspend fun edit(code: Int,orderTypeDto: OrderTypeDto): Resource<String> {
        return try{
            val response = orderTypeApiService.edit(code,orderTypeDto)
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
            val response = orderTypeApiService.delete(code)
            if(response.isSuccessful){
                return Resource.Success(response.body()?.message)
            }
            Resource.Failure(response.errorBody()?.string())
        }catch (_ : IOException){
            Resource.Failure("Please check your network")
        }catch (e : Exception){
            Resource.Failure(e.message ?: "Unknown error")
        }
    }
}