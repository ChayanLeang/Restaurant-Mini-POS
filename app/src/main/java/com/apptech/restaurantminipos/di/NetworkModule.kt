package com.apptech.restaurantminipos.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder().baseUrl("http://172.20.10.5:8080/api/v1/").addConverterFactory(
                                                           GsonConverterFactory.create()).build()
    }
}