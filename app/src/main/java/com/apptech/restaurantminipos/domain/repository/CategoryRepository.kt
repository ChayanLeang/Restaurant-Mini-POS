package com.apptech.restaurantminipos.domain.repository

import com.apptech.restaurantminipos.data.dto.CategoryDto
import com.apptech.restaurantminipos.domain.model.Category
import com.apptech.restaurantminipos.util.Resource

interface CategoryRepository {
    suspend fun getAll() : Resource<List<Category>>
    suspend fun getByCode(code: Int) : Resource<Category>
    suspend fun add(categoryDto: CategoryDto) : Resource<String>
    suspend fun edit(code: Int,categoryDto: CategoryDto) : Resource<String>
    suspend fun delete(code: Int) : Resource<String>
}