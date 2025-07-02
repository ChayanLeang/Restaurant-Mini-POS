package com.apptech.restaurantminipos.domain.model

import java.math.BigDecimal

data class Expense(val code: Int,val name: String,val amount: BigDecimal,val date: String,val time: String,
                   val note: String)