package com.apptech.restaurantminipos.data.repository

import com.apptech.restaurantminipos.data.remote.api.SaleDetailApiService
import com.apptech.restaurantminipos.domain.model.SaleDetail
import com.apptech.restaurantminipos.domain.model.Summary
import com.apptech.restaurantminipos.domain.repository.SaleDetailRepository
import com.apptech.restaurantminipos.util.Resource
import okio.IOException
import java.math.BigDecimal
import javax.inject.Inject

class SaleDetailRepositoryImpl @Inject constructor(private val saleDetailApiService: SaleDetailApiService)
                                                                                  : SaleDetailRepository {
    override suspend fun getBySaleCode(saleCode: Int): Resource<List<SaleDetail>> {
        return try {
            val response = saleDetailApiService.getBySaleCode(saleCode)
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

    override suspend fun getSummaryBySaleCode(saleCode: Int): Summary {
        return try {
            val response = saleDetailApiService.getSummaryByCode(saleCode)
            if(response.isSuccessful){
                return response.body() ?: Summary(BigDecimal.ZERO, BigDecimal.ZERO,BigDecimal.ZERO)
            }
            Summary(BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO)
        }catch (_ : Exception){
            Summary(BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO)
        }
    }
}