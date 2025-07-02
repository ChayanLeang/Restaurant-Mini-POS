package com.apptech.restaurantminipos.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptech.restaurantminipos.data.dto.CustomerDto
import com.apptech.restaurantminipos.domain.model.Customer
import com.apptech.restaurantminipos.domain.repository.CustomerRepository
import com.apptech.restaurantminipos.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(private val customerRepository: CustomerRepository) :
                                                                                    ViewModel() {
    private val _loadedAll = MutableLiveData<Resource<List<Customer>>>(Resource.Loading())
    val loadedAll : LiveData<Resource<List<Customer>>> = _loadedAll;

    private val _loadedByCode = MutableLiveData<Resource<Customer>>(Resource.Loading())
    val loadedByCode : LiveData<Resource<Customer>> = _loadedByCode;

    private val _added = MutableLiveData<Resource<String>>(Resource.Loading())
    val added : LiveData<Resource<String>> = _added;

    private val _edited = MutableLiveData<Resource<String>>(Resource.Loading())
    val edited : LiveData<Resource<String>> = _edited;

    private val _deleted = MutableLiveData<Resource<String>>(Resource.Loading())
    val deleted : LiveData<Resource<String>> = _deleted;

    fun loadAll(){
        viewModelScope.launch {
            _loadedAll.value  = customerRepository.getAll()
        }
    }

    fun loadByCode(code: Int){
        viewModelScope.launch {
            _loadedByCode.value = customerRepository.getByCode(code)
        }
    }

    fun add(customerDto: CustomerDto) {
        viewModelScope.launch {
            _added.value = customerRepository.add(customerDto)
        }
    }

    fun edit(id: Int,customerDto: CustomerDto) {
        viewModelScope.launch {
            _edited.value = customerRepository.edit(id,customerDto)
        }
    }

    fun delete(id: Int){
        viewModelScope.launch {
            _deleted.value = customerRepository.delete(id)
            _loadedAll.value  = customerRepository.getAll()
        }
    }
}