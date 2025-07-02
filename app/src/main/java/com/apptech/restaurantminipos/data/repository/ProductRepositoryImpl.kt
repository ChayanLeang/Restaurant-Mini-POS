package com.apptech.restaurantminipos.data.repository

import com.apptech.restaurantminipos.data.dto.ProductDto
import com.apptech.restaurantminipos.data.remote.api.ProductApiService
import com.apptech.restaurantminipos.data.remote.response.MessageResponse
import com.apptech.restaurantminipos.domain.model.Category
import com.apptech.restaurantminipos.domain.model.Product
import com.apptech.restaurantminipos.domain.repository.ProductRepository
import com.apptech.restaurantminipos.util.Resource
import com.google.gson.Gson
import okio.IOException
import java.math.BigDecimal
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val productApiService: ProductApiService) :
                                                                                ProductRepository {
    override suspend fun getAll(): Resource<List<Product>> {
        return try {
            val response = productApiService.getAll()
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

    override suspend fun getByCode(code: Int): Resource<Product> {
        return try {
            val response = productApiService.getByCode(code)
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

    override suspend fun add(productDto: ProductDto): Resource<String> {
        return try{
            val response = productApiService.add(productDto.image,productDto.code!!,productDto.name,productDto.categoryCode
                                                                                   ,productDto.sellPrice)
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

    override suspend fun edit(code: Int,productDto: ProductDto): Resource<String> {
        return try{
            val response = productApiService.edit(code,productDto.image,productDto.name,productDto
                                                               .categoryCode,productDto.sellPrice)
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
        return Resource.Failure("")
    }

    override suspend fun delete(code: Int): Resource<String> {
        return try {
            val response = productApiService.delete(code)
            if(response.isSuccessful){
                return Resource.Success(response.body()?.message )
            }
            Resource.Failure(response.errorBody()?.string())
        }catch (_ : IOException){
            Resource.Failure("Please check your network")
        }catch (e : Exception){
            Resource.Failure(e.message ?: "Unknown error")
        }
    }
}