package com.apptech.restaurantminipos.util

sealed class Resource<out T> {
    class Loading<T>: Resource<T>()
    data class Success<T>(val data:T ?= null) : Resource<T>()
    data class Failure<T>(val message: String ?= null) : Resource<T>()
}