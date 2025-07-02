package com.apptech.restaurantminipos.domain.repository

import com.apptech.restaurantminipos.data.dto.SaleDto
import com.apptech.restaurantminipos.domain.model.Sale
import com.apptech.restaurantminipos.util.Resource

interface SaleRepository {
    suspend fun getAll() : Resource<List<Sale>>
    suspend fun add(saleDto: SaleDto) : Resource<String>
}