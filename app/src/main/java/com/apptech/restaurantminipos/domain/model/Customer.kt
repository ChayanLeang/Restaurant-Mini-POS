package com.apptech.restaurantminipos.domain.model

import com.google.gson.annotations.SerializedName

data class Customer (val code: Int,
                     val name:String,
                     @SerializedName("phone_number") val phoneNumber:String,
                     val address:String)