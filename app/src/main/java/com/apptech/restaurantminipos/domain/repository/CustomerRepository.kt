package com.apptech.restaurantminipos.domain.repository

import com.apptech.restaurantminipos.data.dto.CustomerDto
import com.apptech.restaurantminipos.domain.model.Customer
import com.apptech.restaurantminipos.util.Resource

interface CustomerRepository {
    suspend fun getAll() : Resource<List<Customer>>
    suspend fun getByCode(code: Int) : Resource<Customer>
    suspend fun add(customerDto: CustomerDto) : Resource<String>
    suspend fun edit(code:Int,customerDto: CustomerDto) : Resource<String>
    suspend fun delete(code: Int) : Resource<String>
}