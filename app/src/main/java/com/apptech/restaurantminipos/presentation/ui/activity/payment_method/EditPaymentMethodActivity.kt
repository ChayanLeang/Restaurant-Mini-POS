package com.apptech.restaurantminipos.presentation.ui.activity.payment_method

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.apptech.restaurantminipos.data.dto.PaymentMethodDto
import com.apptech.restaurantminipos.databinding.ActivityEditPaymentMethodBinding
import com.apptech.restaurantminipos.domain.model.PaymentMethod
import com.apptech.restaurantminipos.presentation.viewmodel.PaymentMethodViewModel
import com.apptech.restaurantminipos.util.DialogUtil
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.TextWatcherUtil
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPaymentMethodActivity : AppCompatActivity() {
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private lateinit var binding: ActivityEditPaymentMethodBinding
    private val paymentMethodViewModel : PaymentMethodViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependency()
        setupLoadPaymentMethodByCode()
    }

    private fun setupDependency(){
        loadingDialogUtil = LoadingDialogUtil(this)
    }

    private fun setupLoadPaymentMethodByCode(){
        val code = intent.getIntExtra("code",0)
        paymentMethodViewModel.loadByCode(code)
        ObserveResource.setup(
            this,
            paymentMethodViewModel.loadedByCode,
            onVisibility = { setupOnVisibility() },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { paymentMethod -> setupOnLoadPaymentMethodByCodeSuccess(paymentMethod) },
            onFailure = { message -> setupOnFailure(message)}
        )
    }

    private fun setupOnVisibility(){
        loadingDialogUtil.show(false)
        binding.sv.visibility = View.GONE
    }

    private fun setupOnLoadPaymentMethodByCodeSuccess(paymentMethod: PaymentMethod){
        binding.apply {
            sv.visibility = View.VISIBLE
            paymentMethodNameTIET.setText(paymentMethod.name)
        }
        initView(paymentMethod.code)
    }

    private fun setupOnFailure(message: String){
        val dialogUtil = DialogUtil(this)
        dialogUtil.showErrorDialog(message)
    }

    private fun initView(code: Int){
        setupValidation()
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            saveMBt.setOnClickListener { setupEditPaymentMethod(code) }
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

    private fun setupEditPaymentMethod(code: Int){
        val paymentMethodName = binding.paymentMethodNameTIET.text.toString()
        paymentMethodViewModel.edit(code, PaymentMethodDto(paymentMethodName))
        setupObserveEditPaymentMethod()
    }

    private fun setupObserveEditPaymentMethod(){
        val loadingDialogUtil = LoadingDialogUtil(this)
        ObserveResource.setup(
            this,
            paymentMethodViewModel.edited,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> setupOnEditPaymentMethodSuccess(message) },
            onFailure = { message -> binding.paymentMethodNameTIL.helperText = message}
        )
    }

    private fun setupOnEditPaymentMethodSuccess(message: String){
        ToastUtil.show(this,message)
        setResult(RESULT_OK)
        finish()
    }
}