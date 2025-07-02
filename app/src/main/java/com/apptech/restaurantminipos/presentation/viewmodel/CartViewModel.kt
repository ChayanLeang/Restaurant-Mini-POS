package com.apptech.restaurantminipos.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptech.restaurantminipos.data.dto.CartDto
import com.apptech.restaurantminipos.domain.model.Cart
import com.apptech.restaurantminipos.domain.repository.CartRepository
import com.apptech.restaurantminipos.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {
    private val _loadedAll = MutableLiveData<Resource<List<Cart>>>(Resource.Loading())
    val loadedAll : LiveData<Resource<List<Cart>>> = _loadedAll

    private val _added = MutableLiveData<Resource<String>>(Resource.Loading())
    val added : LiveData<Resource<String>> = _added

    private val _deleted = MutableLiveData<Resource<String>>(Resource.Loading())
    val deleted : LiveData<Resource<String>> = _deleted

    private val _totalPrice: MutableLiveData<BigDecimal> = MutableLiveData<BigDecimal>()
    val totalPrice : LiveData<BigDecimal> = _totalPrice

    fun loadAll(){
        viewModelScope.launch {
            _loadedAll.value = cartRepository.getAll()
        }
    }

    fun add(cartDto: CartDto){
        viewModelScope.launch {
            _added.value = cartRepository.add(cartDto)
        }
    }

    fun delete(code: Int){
        viewModelScope.launch {
            _deleted.value = cartRepository.delete(code)
            _loadedAll.value = cartRepository.getAll()
        }
    }

    fun loadTotalPrice(){
        viewModelScope.launch {
            _totalPrice.value = cartRepository.getTotalPrice()
        }
    }
}