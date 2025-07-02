package com.apptech.restaurantminipos.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptech.restaurantminipos.data.dto.ExpenseDto
import com.apptech.restaurantminipos.domain.model.Expense
import com.apptech.restaurantminipos.domain.repository.ExpenseRepository
import com.apptech.restaurantminipos.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(private val expenseRepository: ExpenseRepository):
                                                                                ViewModel() {
    private val _loadedAll = MutableLiveData<Resource<List<Expense>>>(Resource.Loading())
    val loadedAll : LiveData<Resource<List<Expense>>> = _loadedAll

    private val _loadedByCode = MutableLiveData<Resource<Expense>>(Resource.Loading())
    val loadedByCode : LiveData<Resource<Expense>> = _loadedByCode

    private val _added = MutableLiveData<Resource<String>>(Resource.Loading())
    val added : LiveData<Resource<String>> = _added

    private val _edited = MutableLiveData<Resource<String>>(Resource.Loading())
    val edited : LiveData<Resource<String>> = _edited

    private val _deleted = MutableLiveData<Resource<String>>(Resource.Loading())
    val deleted : LiveData<Resource<String>> = _deleted

    private val _totalExpense : MutableLiveData<BigDecimal> = MutableLiveData<BigDecimal>()
    val totalExpense : LiveData<BigDecimal> = _totalExpense

    fun loadAll(){
        viewModelScope.launch {
            _loadedAll.value = expenseRepository.getAll()
        }
    }

    fun loadByCode(code: Int){
        viewModelScope.launch {
            _loadedByCode.value = expenseRepository.getByCode(code)
        }
    }

    fun add(expenseDto: ExpenseDto){
        viewModelScope.launch {
            _added.value = expenseRepository.add(expenseDto)
        }
    }

    fun edit(code: Int,expenseDto: ExpenseDto){
        viewModelScope.launch {
            _edited.value = expenseRepository.edit(code,expenseDto)
        }
    }

    fun delete(code: Int){
        viewModelScope.launch {
            _deleted.value = expenseRepository.delete(code)
            _loadedAll.value = expenseRepository.getAll()
        }
    }

    fun loadTotalExpense(){
        viewModelScope.launch {
            _totalExpense.value = expenseRepository.getTotalExpense()
        }
    }
}