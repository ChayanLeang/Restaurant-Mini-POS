package com.apptech.restaurantminipos.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptech.restaurantminipos.data.dto.CategoryDto
import com.apptech.restaurantminipos.domain.model.Category
import com.apptech.restaurantminipos.domain.repository.CategoryRepository
import com.apptech.restaurantminipos.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository):
                                                                                   ViewModel() {
    private val _loadedAll = MutableLiveData<Resource<List<Category>>>(Resource.Loading())
    val loadedAll : LiveData<Resource<List<Category>>> = _loadedAll

    private val _loadedByCode = MutableLiveData<Resource<Category>>(Resource.Loading())
    val loadedByCode : LiveData<Resource<Category>> = _loadedByCode

    private val _added = MutableLiveData<Resource<String>>()
    val added : LiveData<Resource<String>> = _added

    private val _edited = MutableLiveData<Resource<String>>()
    val edited : LiveData<Resource<String>> = _edited

    private val _deleted = MutableLiveData<Resource<String>>()
    val deleted : LiveData<Resource<String>> = _deleted

    fun loadAll(){
        viewModelScope.launch {
            _loadedAll.value = categoryRepository.getAll()
        }
    }

    fun loadByCode(code: Int){
        viewModelScope.launch {
            _loadedByCode.value = categoryRepository.getByCode(code)
        }
    }

    fun add(categoryDto: CategoryDto){
        viewModelScope.launch {
            _added.value = categoryRepository.add(categoryDto)
        }
    }

    fun edit(code: Int,categoryDto: CategoryDto){
        viewModelScope.launch {
            _edited.value = categoryRepository.edit(code,categoryDto)
        }
    }

    fun delete(code: Int){
        viewModelScope.launch {
            _deleted.value = categoryRepository.delete(code)
            _loadedAll.value = categoryRepository.getAll()
        }
    }
}