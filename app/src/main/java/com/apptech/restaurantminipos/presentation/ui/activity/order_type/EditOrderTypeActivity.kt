package com.apptech.restaurantminipos.presentation.ui.activity.order_type

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.apptech.restaurantminipos.data.dto.OrderTypeDto
import com.apptech.restaurantminipos.databinding.ActivityEditOrderTypeBinding
import com.apptech.restaurantminipos.domain.model.OrderType
import com.apptech.restaurantminipos.presentation.viewmodel.OrderTypeViewModel
import com.apptech.restaurantminipos.util.DialogUtil
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.TextWatcherUtil
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditOrderTypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditOrderTypeBinding
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private val orderTypeViewModel : OrderTypeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditOrderTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependency()
        setupLoadOrderTypeByCode()
    }

    private fun setupDependency(){
        loadingDialogUtil = LoadingDialogUtil(this)
    }

    private fun setupLoadOrderTypeByCode(){
        val code = intent.getIntExtra("code",0)
        orderTypeViewModel.loadItemByCode(code)
        ObserveResource.setup(
            this,
            orderTypeViewModel.loadedByCode,
            onVisibility = { setupOnVisibility() },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { orderType -> setupOnLoadOrderTypeByCodeSuccess(orderType) },
            onFailure = { message -> setupOnFailure(message)}
        )
    }

    private fun setupOnVisibility(){
        loadingDialogUtil.show(false)
        binding.sv.visibility = View.GONE
    }

    private fun setupOnLoadOrderTypeByCodeSuccess(orderType: OrderType){
        binding.apply {
            sv.visibility = View.VISIBLE
            orderTypeNameTIET.setText(orderType.name)
        }
        initView(orderType.code)
    }

    private fun setupOnFailure(message: String){
        val dialogUtil = DialogUtil(this)
        dialogUtil.showErrorDialog(message)
    }

    private fun initView(code: Int){
        setupValidation()
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            saveMBt.setOnClickListener { setupEditOrderType(code) }
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

    private fun setupEditOrderType(code: Int){
        val orderTypeName = binding.orderTypeNameTIET.text.toString()
        orderTypeViewModel.editItem(code, OrderTypeDto(orderTypeName))
        setupObserveEditOrderType()
    }

    private fun setupObserveEditOrderType(){
        val loadingDialogUtil = LoadingDialogUtil(this)
        ObserveResource.setup(
            this,
            orderTypeViewModel.edited,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> setupOnEditOrderTypeSuccess(message) },
            onFailure = { message -> binding.orderTypeNameTIL.helperText = message}
        )
    }

    private fun setupOnEditOrderTypeSuccess(message: String){
        ToastUtil.show(this,message)
        setResult(RESULT_OK)
        finish()
    }
}