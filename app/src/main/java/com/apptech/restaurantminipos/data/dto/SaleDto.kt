package com.apptech.restaurantminipos.data.dto

data class SaleDto(val customerCode: Int, val orderTypeCode: Int, val paymentMethodCode: Int,
                   val tableNumber: Int, val discount: Int ?= 0, val products: List<ProductSaleDto>)