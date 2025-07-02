package com.apptech.restaurantminipos.data.dto

import java.math.BigDecimal

data class ExpenseDto (val name: String,val amount: BigDecimal,val date: String,val time: String,
                       val note: String)