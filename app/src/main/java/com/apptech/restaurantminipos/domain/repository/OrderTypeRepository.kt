package com.apptech.restaurantminipos.domain.repository

import com.apptech.restaurantminipos.data.dto.OrderTypeDto
import com.apptech.restaurantminipos.domain.model.OrderType
import com.apptech.restaurantminipos.util.Resource

interface OrderTypeRepository {
    suspend fun getAll() : Resource<List<OrderType>>
    suspend fun getByCode(code: Int) : Resource<OrderType>
    suspend fun add(orderTypeDto: OrderTypeDto) : Resource<String>
    suspend fun edit(code: Int,orderTypeDto: OrderTypeDto) : Resource<String>
    suspend fun delete(code: Int) : Resource<String>
}