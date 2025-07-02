package com.apptech.restaurantminipos.data.repository

import com.apptech.restaurantminipos.data.dto.CategoryDto
import com.apptech.restaurantminipos.data.remote.api.CategoryApiService
import com.apptech.restaurantminipos.data.remote.response.MessageResponse
import com.apptech.restaurantminipos.domain.model.Category
import com.apptech.restaurantminipos.domain.repository.CategoryRepository
import com.apptech.restaurantminipos.util.Resource
import com.google.gson.Gson
import okio.IOException
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val categoryApiService: CategoryApiService) :
                                                                                  CategoryRepository {
    override suspend fun getAll(): Resource<List<Category>> {
        return try {
            val response = categoryApiService.getAll()
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

    override suspend fun getByCode(code: Int): Resource<Category> {
        return try {
            val response = categoryApiService.getByCode(code)
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

    override suspend fun add(categoryDto: CategoryDto): Resource<String> {
        return try{
            val response = categoryApiService.add(categoryDto)
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

    override suspend fun edit(code: Int,categoryDto: CategoryDto): Resource<String> {
        return try{
            val response = categoryApiService.edit(code,categoryDto)
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
            val response = categoryApiService.delete(code)
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
}