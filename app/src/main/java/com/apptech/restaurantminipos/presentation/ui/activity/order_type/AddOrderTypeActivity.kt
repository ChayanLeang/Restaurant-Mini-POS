package com.apptech.restaurantminipos.presentation.ui.activity.order_type

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.apptech.restaurantminipos.data.dto.OrderTypeDto
import com.apptech.restaurantminipos.databinding.ActivityAddOrderTypeBinding
import com.apptech.restaurantminipos.presentation.viewmodel.OrderTypeViewModel
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.TextWatcherUtil
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddOrderTypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddOrderTypeBinding
    private val orderTypeViewModel: OrderTypeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOrderTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        setupValidation()
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            saveMBt.setOnClickListener { setupAddOrderType() }
        }
    }

    private fun setupValidation(){
        val textWatcher = TextWatcherUtil.setup {
            val orderTypeName = binding.orderTypeNameTIET.text.toString()
            val enable = !TextUtils.isEmpty(orderTypeName)
            binding.apply {
                saveMBt.alpha = if(enable) 1f else 0.5f
                saveMBt.isEnabled = enable
            }
        }
        binding.orderTypeNameTIET.addTextChangedListener(textWatcher)
    }

    private fun setupAddOrderType(){
        val orderTypeName = binding.orderTypeNameTIET.text.toString()
        orderTypeViewModel.addItem(OrderTypeDto(orderTypeName))
        setupObserveAddOrderType()
    }

    private fun setupObserveAddOrderType(){
        val loadingDialogUtil = LoadingDialogUtil(this)
        ObserveResource.setup(
            this,
            orderTypeViewModel.added,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> setupOnSuccess(message)},
            onFailure = { message -> binding.orderTypeNameTIL.helperText = message }
        )
    }

    private fun setupOnSuccess(message: String){
        ToastUtil.show(this,message)
        setResult(RESULT_OK)
        finish()
    }
}