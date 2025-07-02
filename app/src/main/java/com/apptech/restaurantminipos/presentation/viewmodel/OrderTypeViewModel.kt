package com.apptech.restaurantminipos.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptech.restaurantminipos.data.dto.OrderTypeDto
import com.apptech.restaurantminipos.domain.model.OrderType
import com.apptech.restaurantminipos.domain.repository.OrderTypeRepository
import com.apptech.restaurantminipos.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderTypeViewModel @Inject constructor(private val orderTypeRepository: OrderTypeRepository):
                                                                                      ViewModel() {
    private val _loadedAll = MutableLiveData<Resource<List<OrderType>>>(Resource.Loading())
    val loadedAll : LiveData<Resource<List<OrderType>>> = _loadedAll

    private val _loadedByCode = MutableLiveData<Resource<OrderType>>(Resource.Loading())
    val loadedByCode : LiveData<Resource<OrderType>> = _loadedByCode

    private val _added = MutableLiveData<Resource<String>>(Resource.Loading())
    val added : LiveData<Resource<String>> = _added

    private val _edited = MutableLiveData<Resource<String>>(Resource.Loading())
    val edited : LiveData<Resource<String>> = _edited

    private val _deleted = MutableLiveData<Resource<String>>(Resource.Loading())
    val deleted : LiveData<Resource<String>> = _deleted

    fun loadItems(){
        viewModelScope.launch {
            _loadedAll.value = orderTypeRepository.getAll()
        }
    }

    fun loadItemByCode(code: Int){
        viewModelScope.launch {
            _loadedByCode.value = orderTypeRepository.getByCode(code)
        }
    }

    fun addItem(orderTypeDto: OrderTypeDto){
        viewModelScope.launch {
            _added.value = orderTypeRepository.add(orderTypeDto)
        }
    }

    fun editItem(code: Int,orderTypeDto: OrderTypeDto){
        viewModelScope.launch {
            _edited.value = orderTypeRepository.edit(code,orderTypeDto)
        }
    }

    fun deleteItem(code: Int){
        viewModelScope.launch {
            _deleted.value = orderTypeRepository.delete(code)
            _loadedAll.value = orderTypeRepository.getAll()
        }
    }
}