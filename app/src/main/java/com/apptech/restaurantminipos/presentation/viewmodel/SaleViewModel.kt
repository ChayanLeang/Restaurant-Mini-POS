package com.apptech.restaurantminipos.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptech.restaurantminipos.data.dto.SaleDto
import com.apptech.restaurantminipos.domain.model.Sale
import com.apptech.restaurantminipos.domain.repository.SaleRepository
import com.apptech.restaurantminipos.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaleViewModel @Inject constructor(private val saleRepository: SaleRepository) : ViewModel() {
    private val _loadedAll = MutableLiveData<Resource<List<Sale>>>(Resource.Loading())
    val loadedAll : LiveData<Resource<List<Sale>>> = _loadedAll

    private val _added = MutableLiveData<Resource<String>>(Resource.Loading())
    val added : LiveData<Resource<String>> = _added

    fun loadAll(){
        viewModelScope.launch {
            _loadedAll.value = saleRepository.getAll()
        }
    }

    fun add(saleDto: SaleDto){
        viewModelScope.launch {
            _added.value = saleRepository.add(saleDto)
        }
    }
 }