package com.apptech.restaurantminipos.domain.repository

import com.apptech.restaurantminipos.data.dto.PaymentMethodDto
import com.apptech.restaurantminipos.domain.model.PaymentMethod
import com.apptech.restaurantminipos.util.Resource

interface PaymentMethodRepository {
    suspend fun getAll() : Resource<List<PaymentMethod>>
    suspend fun getByCode(code: Int) : Resource<PaymentMethod>
    suspend fun add(paymentMethodDto: PaymentMethodDto) : Resource<String>
    suspend fun edit(code: Int,paymentMethodDto: PaymentMethodDto) : Resource<String>
    suspend fun delete(code: Int) : Resource<String>
}