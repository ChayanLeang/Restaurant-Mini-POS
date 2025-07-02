package com.apptech.restaurantminipos.domain.repository

import com.apptech.restaurantminipos.data.dto.CartDto
import com.apptech.restaurantminipos.domain.model.Cart
import com.apptech.restaurantminipos.util.Resource
import java.math.BigDecimal

interface CartRepository {
    suspend fun getAll() : Resource<List<Cart>>
    suspend fun add(cartDto: CartDto) : Resource<String>
    suspend fun delete(code: Int) : Resource<String>
    suspend fun getTotalPrice() : BigDecimal
}