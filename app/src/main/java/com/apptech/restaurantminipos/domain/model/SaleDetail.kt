package com.apptech.restaurantminipos.domain.model

import java.math.BigDecimal

data class SaleDetail(val code: Int,
                      val quantity: Int,
                      val amount: BigDecimal,
                      val product: Product)