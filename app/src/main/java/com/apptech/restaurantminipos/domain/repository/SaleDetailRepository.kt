package com.apptech.restaurantminipos.domain.repository

import com.apptech.restaurantminipos.domain.model.SaleDetail
import com.apptech.restaurantminipos.domain.model.Summary
import com.apptech.restaurantminipos.util.Resource

interface SaleDetailRepository {
    suspend fun getBySaleCode(saleCode: Int) : Resource<List<SaleDetail>>
    suspend fun getSummaryBySaleCode(saleCode: Int) : Summary
}