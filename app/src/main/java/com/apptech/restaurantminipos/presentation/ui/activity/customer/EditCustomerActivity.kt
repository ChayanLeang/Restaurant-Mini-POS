package com.apptech.restaurantminipos.presentation.ui.activity.customer

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.apptech.restaurantminipos.data.dto.CustomerDto
import com.apptech.restaurantminipos.databinding.ActivityEditCustomerBinding
import com.apptech.restaurantminipos.domain.model.Customer
import com.apptech.restaurantminipos.presentation.viewmodel.CustomerViewModel
import com.apptech.restaurantminipos.util.DialogUtil
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.TextWatcherUtil
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCustomerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditCustomerBinding
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private val customerViewModel : CustomerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependency()
        setupLoadCustomerByCode()
    }

    private fun setupDependency(){
        loadingDialogUtil = LoadingDialogUtil(this)
    }

    private fun setupLoadCustomerByCode(){
        val code: Int = intent.getIntExtra("code",0)
        customerViewModel.loadByCode(code)
        ObserveResource.setup(
            this,
            customerViewModel.loadedByCode,
            onVisibility = { setupOnVisibility() },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { customer -> setupLoadCustomerByCodeSuccess(customer) },
            onFailure = { message -> setupOnFailure(message) },
        )
    }

    private fun setupOnVisibility(){
        loadingDialogUtil.show(false)
        binding.sv.visibility = View.GONE
    }

    private fun setupLoadCustomerByCodeSuccess(customer: Customer){
        binding.apply {
            sv.visibility = View.VISIBLE
            customerNameTIET.setText(customer.name)
            phoneNumberTIET.setText(customer.phoneNumber)
            addressTIET.setText(customer.address)
        }
        initView(customer.code)
    }

    private fun initView(code: Int){
        setupValidation()
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            saveMBt.setOnClickListener { setupEditCustomer(code) }
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

    private fun setupEditCustomer(code: Int){
        val name = binding.customerNameTIET.text.toString()
        val phoneNumber = binding.phoneNumberTIET.text.toString()
        val address = binding.addressTIET.text.toString()
        customerViewModel.edit(code, CustomerDto(name,phoneNumber,address))
        setupObserveEditCustomer()
    }

    private fun setupObserveEditCustomer(){
        val loadingDialogUtil = LoadingDialogUtil(this)
        ObserveResource.setup(
            this,
            customerViewModel.edited,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> setupEditCustomerSuccess(message)},
            onFailure = { message -> binding.phoneNumberTIL.helperText = message}
        )
    }

    private fun setupEditCustomerSuccess(message: String){
        ToastUtil.show(this,message)
        setResult(RESULT_OK)
        finish()
    }

    private fun setupOnFailure(message: String){
        val dialogUtil = DialogUtil(this)
        dialogUtil.showErrorDialog(message)
    }
}