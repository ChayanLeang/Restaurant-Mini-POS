package com.apptech.restaurantminipos.domain.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Product(val code: Int,
                   val name: String,
                   @SerializedName("sell_price") val sellPrice: BigDecimal,
                   val category: Category,
                   @SerializedName("image_url") val imageUrl: String)