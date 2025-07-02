package com.apptech.restaurantminipos.domain.repository

import com.apptech.restaurantminipos.data.dto.ProductDto
import com.apptech.restaurantminipos.domain.model.Product
import com.apptech.restaurantminipos.util.Resource

interface ProductRepository {
    suspend fun getAll() : Resource<List<Product>>
    suspend fun getByCode(code: Int) : Resource<Product>
    suspend fun add(productDto: ProductDto) : Resource<String>
    suspend fun edit(code: Int,productDto: ProductDto) : Resource<String>
    suspend fun delete(code: Int) : Resource<String>
}