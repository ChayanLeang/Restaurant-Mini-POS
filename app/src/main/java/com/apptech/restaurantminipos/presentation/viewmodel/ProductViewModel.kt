package com.apptech.restaurantminipos.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptech.restaurantminipos.data.dto.ProductDto
import com.apptech.restaurantminipos.domain.model.Product
import com.apptech.restaurantminipos.domain.repository.ProductRepository
import com.apptech.restaurantminipos.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val productRepository: ProductRepository):
                                                                                ViewModel() {
    private val _loadedAll = MutableLiveData<Resource<List<Product>>>(Resource.Loading())
    val loadedAll : LiveData<Resource<List<Product>>> = _loadedAll

    private val _loadedByCode = MutableLiveData<Resource<Product>>(Resource.Loading())
    val loadedByCode : LiveData<Resource<Product>> = _loadedByCode

    private val _added = MutableLiveData<Resource<String>>(Resource.Loading())
    val added : LiveData<Resource<String>> = _added

    private val _edited = MutableLiveData<Resource<String>>(Resource.Loading())
    val edited : LiveData<Resource<String>> = _edited

    private val _deleted = MutableLiveData<Resource<String>>(Resource.Loading())
    val deleted : LiveData<Resource<String>> = _deleted

    fun loadAll(){
        viewModelScope.launch {
            _loadedAll.value = productRepository.getAll()
        }
    }

    fun loadByCode(code: Int){
        viewModelScope.launch {
            _loadedByCode.value = productRepository.getByCode(code)
        }
    }

    fun add(productDto: ProductDto){
        viewModelScope.launch {
            _added.value = productRepository.add(productDto)
        }
    }

    fun edit(code: Int,productDto: ProductDto){
        viewModelScope.launch {
            _edited.value = productRepository.edit(code,productDto)
        }
    }

    fun delete(code: Int){
        viewModelScope.launch {
            _deleted.value = productRepository.delete(code)
            _loadedAll.value = productRepository.getAll()
        }
    }
}