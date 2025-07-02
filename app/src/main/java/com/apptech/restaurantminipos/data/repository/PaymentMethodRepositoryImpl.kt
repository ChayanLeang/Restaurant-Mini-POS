package com.apptech.restaurantminipos.data.repository

import com.apptech.restaurantminipos.data.dto.PaymentMethodDto
import com.apptech.restaurantminipos.data.remote.api.PaymentMethodApiService
import com.apptech.restaurantminipos.data.remote.response.MessageResponse
import com.apptech.restaurantminipos.domain.model.PaymentMethod
import com.apptech.restaurantminipos.domain.repository.PaymentMethodRepository
import com.apptech.restaurantminipos.util.Resource
import com.google.gson.Gson
import okio.IOException
import javax.inject.Inject

class PaymentMethodRepositoryImpl @Inject constructor(private val paymentMethodApiService:
                                                      PaymentMethodApiService):PaymentMethodRepository {

    override suspend fun getAll() : Resource<List<PaymentMethod>> {
        return try {
            val response = paymentMethodApiService.getAll()
            if(response.isSuccessful){
                return Resource.Success(response.body() ?: emptyList())
            }
            Resource.Failure("")
        }catch (_ : IOException){
            Resource.Failure("Please check your network")
        }catch (e : Exception){
            Resource.Failure(e.message ?: "Unknown Error")
        }
    }

    override suspend fun getByCode(code: Int): Resource<PaymentMethod> {
        return try {
            val response = paymentMethodApiService.getByCode(code)
            if(response.isSuccessful){
                return Resource.Success(response.body())
            }
            Resource.Failure(response.errorBody()?.string().toString())
        }catch (_ : IOException){
            Resource.Failure("Please check your network")
        }catch (e : Exception){
            Resource.Failure(e.message ?: "Unknown Error")
        }
    }

    override suspend fun add(paymentMethodDto: PaymentMethodDto): Resource<String> {
        return try{
            val response = paymentMethodApiService.add(paymentMethodDto)
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

    override suspend fun edit(code: Int,paymentMethodDto: PaymentMethodDto): Resource<String> {
        return try {
            val response = paymentMethodApiService.edit(code,paymentMethodDto)
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
            val response = paymentMethodApiService.delete(code)
            if(response.isSuccessful){
                return Resource.Success(response.body()?.message)
            }
            Resource.Failure(response.errorBody()?.string().toString())
        }catch (_ : IOException){
            Resource.Failure("Please check your network")
        }catch (e : Exception){
            Resource.Failure(e.message ?: "Unknown Error")
        }
    }
}