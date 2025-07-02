package com.apptech.restaurantminipos.presentation.ui.activity.category

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.apptech.restaurantminipos.data.dto.CategoryDto
import com.apptech.restaurantminipos.databinding.ActivityEditCategoryBinding
import com.apptech.restaurantminipos.domain.model.Category
import com.apptech.restaurantminipos.presentation.viewmodel.CategoryViewModel
import com.apptech.restaurantminipos.util.DialogUtil
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.TextWatcherUtil
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditCategoryBinding
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private val categoryViewModel : CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependency()
        setupLoadCategoryByCode()
    }

    private fun setupDependency(){
        loadingDialogUtil = LoadingDialogUtil(this)
    }

    private fun setupLoadCategoryByCode(){
        val code = intent.getIntExtra("code",0)
        categoryViewModel.loadByCode(code)
        ObserveResource.setup(
            this,
            categoryViewModel.loadedByCode,
            onVisibility = { setupOnVisibility() },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { category -> setupOnLoadCategoryByCodeSuccess(category) },
            onFailure = { message -> setupOnFailure(message)}
        )
    }

    private fun setupOnVisibility(){
        loadingDialogUtil.show(false)
        binding.sv.visibility = View.GONE
    }

    private fun setupOnLoadCategoryByCodeSuccess(category: Category){
        binding.apply {
            sv.visibility = View.VISIBLE
            categoryNameTIET.setText(category.name)
        }
        initView(category.code)
    }

    private fun setupOnFailure(message: String){
        val dialogUtil = DialogUtil(this)
        dialogUtil.showErrorDialog(message)
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
            val categoryName = binding.categoryNameTIET.text.toString()
            val enable: Boolean = !TextUtils.isEmpty(categoryName)
            binding.apply {
                saveMBt.alpha = if(enable) 1f else 0.5f
                saveMBt.isEnabled = enable
            }
        }
        binding.categoryNameTIET.addTextChangedListener(textWatcher)
    }

    private fun setupEditCustomer(code: Int){
        val categoryName = binding.categoryNameTIET.text.toString()
        categoryViewModel.edit(code, CategoryDto(categoryName))
        setupObserveEditCustomer()
    }

    private fun setupObserveEditCustomer(){
        val loadingDialogUtil = LoadingDialogUtil(this)
        ObserveResource.setup(
            this,
            categoryViewModel.edited,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> setupOnEditCustomerSuccess(message) },
            onFailure = { message -> binding.categoryNameTIL.helperText = message}
        )
    }

    private fun setupOnEditCustomerSuccess(message: String){
        ToastUtil.show(this,message)
        setResult(RESULT_OK)
        finish()
    }
}