package com.apptech.restaurantminipos.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

object ObserveResource {

    fun <T> setup(
        lifecycleOwner: LifecycleOwner, liveData: LiveData<Resource<T>>, onVisibility: () -> Unit,
        onLoading: () -> Unit, onSuccess: (T) -> Unit, onFailure: (String) -> Unit
    ){
        liveData.observe(lifecycleOwner) { resource ->
            onVisibility()
            when(resource){
                is Resource.Loading -> onLoading()

                is Resource.Success -> onSuccess(resource.data!!)

                is Resource.Failure -> onFailure(resource.message!!)
            }
        }
    }
}