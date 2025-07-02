package com.apptech.restaurantminipos.data.repository

import com.apptech.restaurantminipos.data.dto.CustomerDto
import com.apptech.restaurantminipos.data.remote.api.CustomerApiService
import com.apptech.restaurantminipos.data.remote.response.MessageResponse
import com.apptech.restaurantminipos.domain.model.Customer
import com.apptech.restaurantminipos.domain.repository.CustomerRepository
import com.apptech.restaurantminipos.util.Resource
import com.google.gson.Gson
import okio.IOException
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(private val customerApiService: CustomerApiService) :
                                                                                  CustomerRepository {
    override suspend fun getAll() : Resource<List<Customer>> {
        return try {
            val response = customerApiService.getAll()
            if(response.isSuccessful){
                return Resource.Success(response.body() ?: emptyList())
            }
            Resource.Failure()
        }catch (_ : IOException){
            Resource.Failure("Please check your network")
        }catch (e : Exception){
            Resource.Failure(e.message ?: "Unknown Error")
        }
    }

    override suspend fun getByCode(code: Int): Resource<Customer> {
        return try {
            val response = customerApiService.getByCode(code)
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

    override suspend fun add(customerDto: CustomerDto): Resource<String> {
        return try{
            val response = customerApiService.add(customerDto)
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

    override suspend fun edit(code: Int,customerDto: CustomerDto): Resource<String> {
        return try {
            val response = customerApiService.edit(code,customerDto)
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
            val response = customerApiService.delete(code)
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