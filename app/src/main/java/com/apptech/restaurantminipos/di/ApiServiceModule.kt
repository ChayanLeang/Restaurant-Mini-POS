package com.apptech.restaurantminipos.di

import com.apptech.restaurantminipos.data.remote.api.CartApiService
import com.apptech.restaurantminipos.data.remote.api.CategoryApiService
import com.apptech.restaurantminipos.data.remote.api.CustomerApiService
import com.apptech.restaurantminipos.data.remote.api.ExpenseApiService
import com.apptech.restaurantminipos.data.remote.api.OrderTypeApiService
import com.apptech.restaurantminipos.data.remote.api.PaymentMethodApiService
import com.apptech.restaurantminipos.data.remote.api.ProductApiService
import com.apptech.restaurantminipos.data.remote.api.SaleApiService
import com.apptech.restaurantminipos.data.remote.api.SaleDetailApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiServiceModule {

    @Provides
    @Singleton
    fun provideCustomerApiService(retrofit: Retrofit) : CustomerApiService{
        return retrofit.create(CustomerApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductApiService(retrofit: Retrofit) : ProductApiService {
        return retrofit.create(ProductApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCartApiService(retrofit: Retrofit) : CartApiService {
        return retrofit.create(CartApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideExpenseApiService(retrofit: Retrofit) : ExpenseApiService {
        return retrofit.create(ExpenseApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSaleApiService(retrofit: Retrofit) : SaleApiService {
        return retrofit.create(SaleApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSaleDetailApiService(retrofit: Retrofit) : SaleDetailApiService {
        return retrofit.create(SaleDetailApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryApiService(retrofit: Retrofit) : CategoryApiService {
        return retrofit.create(CategoryApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePaymentMethodApiService(retrofit: Retrofit) : PaymentMethodApiService {
        return retrofit.create(PaymentMethodApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOrderTypeApiService(retrofit: Retrofit) : OrderTypeApiService {
        return retrofit.create(OrderTypeApiService::class.java)
    }
}