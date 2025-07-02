package com.apptech.restaurantminipos.data.repository

import com.apptech.restaurantminipos.data.dto.CartDto
import com.apptech.restaurantminipos.data.remote.api.CartApiService
import com.apptech.restaurantminipos.data.remote.response.MessageResponse
import com.apptech.restaurantminipos.domain.model.Cart
import com.apptech.restaurantminipos.domain.repository.CartRepository
import com.apptech.restaurantminipos.util.Resource
import com.google.gson.Gson
import okio.IOException
import java.math.BigDecimal
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val cartApiService: CartApiService) :
                                                                          CartRepository {
    override suspend fun getAll(): Resource<List<Cart>> {
        return try {
            val response = cartApiService.getAll()
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

    override suspend fun add(cartDto: CartDto): Resource<String> {
        return try{
            val response = cartApiService.add(cartDto)
            if(response.isSuccessful){
                return Resource.Success(response.body()?.message ?: "")
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
            val response = cartApiService.delete(code)
            if(response.isSuccessful){
                return Resource.Success(response.body()?.message ?: "")
            }
            Resource.Failure(response.errorBody()?.string() ?: "")
        }catch (_ : IOException){
            Resource.Failure("Please check your network")
        }catch (e : Exception){
            Resource.Failure(e.message ?: "Unknown error")
        }
    }

    override suspend fun getTotalPrice(): BigDecimal {
        return try {
            val response = cartApiService.getTotalPrice()
            if(response.isSuccessful){
                return response.body() ?: BigDecimal.ZERO
            }
            BigDecimal.ZERO
        }catch (_ : Exception){
            BigDecimal.ZERO
        }
    }
}