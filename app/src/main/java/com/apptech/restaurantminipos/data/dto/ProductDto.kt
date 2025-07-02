package com.apptech.restaurantminipos.data.dto

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class ProductDto(val image: MultipartBody.Part,val code: RequestBody?, val name: RequestBody,
                      val categoryCode: RequestBody,val sellPrice: RequestBody)