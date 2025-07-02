package com.apptech.restaurantminipos.domain.repository

import com.apptech.restaurantminipos.data.dto.ExpenseDto
import com.apptech.restaurantminipos.domain.model.Expense
import com.apptech.restaurantminipos.util.Resource
import java.math.BigDecimal

interface ExpenseRepository {
    suspend fun getAll() : Resource<List<Expense>>
    suspend fun getByCode(code: Int) : Resource<Expense>
    suspend fun add(expenseDto: ExpenseDto) : Resource<String>
    suspend fun edit(code: Int,expenseDto: ExpenseDto) : Resource<String>
    suspend fun delete(code: Int) : Resource<String>
    suspend fun getTotalExpense() : BigDecimal
}