package com.apptech.restaurantminipos.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptech.restaurantminipos.data.dto.PaymentMethodDto
import com.apptech.restaurantminipos.domain.model.PaymentMethod
import com.apptech.restaurantminipos.domain.repository.PaymentMethodRepository
import com.apptech.restaurantminipos.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentMethodViewModel @Inject constructor(private val paymentMethodRepository:
                                                 PaymentMethodRepository): ViewModel() {
    private val _loadedAll = MutableLiveData<Resource<List<PaymentMethod>>>(Resource.Loading())
    val loadedAll : LiveData<Resource<List<PaymentMethod>>> = _loadedAll

    private val _loadedByCode = MutableLiveData<Resource<PaymentMethod>>(Resource.Loading())
    val loadedByCode : LiveData<Resource<PaymentMethod>> = _loadedByCode

    private val _added = MutableLiveData<Resource<String>>(Resource.Loading())
    val added : LiveData<Resource<String>> = _added

    private val _edited = MutableLiveData<Resource<String>>(Resource.Loading())
    val edited : LiveData<Resource<String>> = _edited

    private val _deleted = MutableLiveData<Resource<String>>(Resource.Loading())
    val deleted : LiveData<Resource<String>> = _deleted

    fun loadAll(){
        viewModelScope.launch {
            _loadedAll.value = paymentMethodRepository.getAll()
        }
    }

    fun loadByCode(code: Int){
        viewModelScope.launch {
            _loadedByCode.value = paymentMethodRepository.getByCode(code)
        }
    }

    fun add(paymentMethodDto: PaymentMethodDto){
        viewModelScope.launch {
            _added.value = paymentMethodRepository.add(paymentMethodDto)
        }
    }

    fun edit(code: Int,paymentMethodDto: PaymentMethodDto){
        viewModelScope.launch {
            _edited.value = paymentMethodRepository.edit(code,paymentMethodDto)
        }
    }

    fun delete(code: Int){
        viewModelScope.launch {
            _deleted.value = paymentMethodRepository.delete(code)
            _loadedAll.value = paymentMethodRepository.getAll()
        }
    }
}