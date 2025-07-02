package com.apptech.restaurantminipos.presentation.ui.activity.customer

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.apptech.restaurantminipos.data.dto.CustomerDto
import com.apptech.restaurantminipos.databinding.ActivityAddCustomerBinding
import com.apptech.restaurantminipos.presentation.viewmodel.CustomerViewModel
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.TextWatcherUtil
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCustomerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCustomerBinding
    private val customerViewModel : CustomerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        setupValidation()
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            saveMBt.setOnClickListener { setupAddCustomer() }
        }
    }

    private fun setupValidation(){
        val textWatcher = TextWatcherUtil.setup {
            val customerName = binding.customerNameTIET.text.toString()
            val phoneNumber = binding.phoneNumberTIET.text.toString()
            val enable = !TextUtils.isEmpty(customerName) && !TextUtils.isEmpty(phoneNumber)
            binding.apply {
                saveMBt.alpha = if(enable) 1f else 0.5f
                saveMBt.isEnabled = enable
            }
        }
        binding.apply {
            customerNameTIET.addTextChangedListener(textWatcher)
            phoneNumberTIET.addTextChangedListener(textWatcher)
        }
    }

    private fun setupAddCustomer(){
        val name = binding.customerNameTIET.text.toString()
        val phoneNumber = binding.phoneNumberTIET.text.toString()
        val address = binding.addressTIET.text.toString()
        customerViewModel.add(CustomerDto(name,phoneNumber,address))
        setupObserveAddCustomer()
    }

    private fun setupObserveAddCustomer(){
        val loadingDialogUtil = LoadingDialogUtil(this)
        ObserveResource.setup(
            this,
            customerViewModel.added,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> setupOnSuccess(message) },
            onFailure = { message -> binding.phoneNumberTIL.helperText = message }
        )
    }

    private fun setupOnSuccess(message: String){
        ToastUtil.show(this,message)
        setResult(RESULT_OK)
        finish()
    }
}