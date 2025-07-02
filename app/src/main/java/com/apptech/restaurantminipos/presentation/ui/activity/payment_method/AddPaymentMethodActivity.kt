package com.apptech.restaurantminipos.presentation.ui.activity.payment_method

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.apptech.restaurantminipos.data.dto.PaymentMethodDto
import com.apptech.restaurantminipos.databinding.ActivityAddPaymentMethodBinding
import com.apptech.restaurantminipos.presentation.viewmodel.PaymentMethodViewModel
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.TextWatcherUtil
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPaymentMethodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPaymentMethodBinding
    private val paymentMethodViewModel: PaymentMethodViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        setupValidation()
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            saveMBt.setOnClickListener { setupAddPaymentMethod() }
        }
    }

    private fun setupValidation(){
        val textWatcher = TextWatcherUtil.setup {
            val paymentMethodName = binding.paymentMethodNameTIET.text.toString()
            val enable = !TextUtils.isEmpty(paymentMethodName)
            binding.apply {
                saveMBt.alpha = if(enable) 1f else 0.5f
                saveMBt.isEnabled = enable
            }
        }
        binding.paymentMethodNameTIET.addTextChangedListener(textWatcher)
    }

    private fun setupAddPaymentMethod(){
        val paymentMethodName = binding.paymentMethodNameTIET.text.toString()
        paymentMethodViewModel.add(PaymentMethodDto(paymentMethodName))
        setupObserveAddPaymentMethod()
    }

    private fun setupObserveAddPaymentMethod(){
        val loadingDialogUtil = LoadingDialogUtil(this)
        ObserveResource.setup(
            this,
            paymentMethodViewModel.added,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> setupOnSuccess(message)},
            onFailure = { message -> binding.paymentMethodNameTIL.helperText = message }
        )
    }

    private fun setupOnSuccess(message: String){
        ToastUtil.show(this,message)
        setResult(RESULT_OK)
        finish()
    }
}