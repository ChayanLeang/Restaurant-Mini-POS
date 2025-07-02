package com.apptech.restaurantminipos.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.apptech.restaurantminipos.R
import com.apptech.restaurantminipos.data.dto.SaleDto
import com.apptech.restaurantminipos.databinding.FragmentPaymentDialogBinding
import com.apptech.restaurantminipos.presentation.viewmodel.SaleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentDialogFragment : DialogFragment() {
    private val saleViewModel : SaleViewModel by viewModels()
    private lateinit var binding: FragmentPaymentDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPaymentDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView(){
        binding.apply {
            mtb.setNavigationOnClickListener { dismiss() }
            mtb.setOnMenuItemClickListener { item ->
                if(item.itemId == R.id.submit){
                    setupOnSubmit()
                }
                true
            }
        }
    }

    private fun setupOnSubmit(){
        val tableNumber = binding.tableNumberTIET.text.toString().toInt()
        val customer = binding.customerTV.text.toString().toInt()
        val orderType = binding.orderTypeTV.text.toString().toInt()
        val paymentMethod = binding.paymentMethodTV.text.toString().toInt()
        val discount = binding.discountTIET.text.toString().toInt()
        saleViewModel.add(SaleDto(customer,orderType,paymentMethod,tableNumber,discount,emptyList()))
    }
}