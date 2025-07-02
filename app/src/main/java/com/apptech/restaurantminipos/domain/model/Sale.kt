package com.apptech.restaurantminipos.domain.model

import com.google.gson.annotations.SerializedName

data class Sale(val code: Int,
                @SerializedName("date_time") val dateTime: String,
                @SerializedName("table_number") val tableNumber: Int,
                val discount: Int,
                val customer: Customer,
                @SerializedName("order_type") val orderType: OrderType,
                @SerializedName("payment_method") val paymentMethod: PaymentMethod)