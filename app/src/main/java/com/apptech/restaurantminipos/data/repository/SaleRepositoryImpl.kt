package com.apptech.restaurantminipos.data.repository

import com.apptech.restaurantminipos.data.dto.SaleDto
import com.apptech.restaurantminipos.data.remote.api.SaleApiService
import com.apptech.restaurantminipos.data.remote.response.MessageResponse
import com.apptech.restaurantminipos.domain.model.Sale
import com.apptech.restaurantminipos.domain.repository.SaleRepository
import com.apptech.restaurantminipos.util.Resource
import com.google.gson.Gson
import okio.IOException
import javax.inject.Inject

class SaleRepositoryImpl @Inject constructor(private val saleApiService: SaleApiService) : SaleRepository {

    override suspend fun getAll(): Resource<List<Sale>> {
        return try {
            val response = saleApiService.getAll()
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

    override suspend fun add(saleDto: SaleDto): Resource<String> {
        return try{
            val response = saleApiService.add(saleDto)
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
}