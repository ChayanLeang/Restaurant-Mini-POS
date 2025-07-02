package com.apptech.restaurantminipos.domain.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Summary(@SerializedName("sub_total") val subTotal: BigDecimal,
                   val discount: BigDecimal,
                   @SerializedName("total_price") val totalPrice: BigDecimal)