package com.apptech.restaurantminipos.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptech.restaurantminipos.domain.model.SaleDetail
import com.apptech.restaurantminipos.domain.model.Summary
import com.apptech.restaurantminipos.domain.repository.SaleDetailRepository
import com.apptech.restaurantminipos.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaleDetailViewModel @Inject constructor(private val saleDetailRepository: SaleDetailRepository) :
                                                                                          ViewModel() {
    private val _loadedAll = MutableLiveData<Resource<List<SaleDetail>>>(Resource.Loading())
    val loadedAll : LiveData<Resource<List<SaleDetail>>> = _loadedAll

    private val _summary = MutableLiveData<Summary>()
    val summary : LiveData<Summary> = _summary

    fun loadBySaleCode(saleCode: Int){
        viewModelScope.launch {
            _loadedAll.value = saleDetailRepository.getBySaleCode(saleCode)
        }
    }

    fun loadSummary(saleCode: Int){
        viewModelScope.launch {
            _summary.value = saleDetailRepository.getSummaryBySaleCode(saleCode)
        }
    }
}