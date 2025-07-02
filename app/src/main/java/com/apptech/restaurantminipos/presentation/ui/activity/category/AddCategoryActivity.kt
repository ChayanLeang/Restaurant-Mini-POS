package com.apptech.restaurantminipos.presentation.ui.activity.category

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.apptech.restaurantminipos.data.dto.CategoryDto
import com.apptech.restaurantminipos.databinding.ActivityAddCategoryBinding
import com.apptech.restaurantminipos.presentation.viewmodel.CategoryViewModel
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.TextWatcherUtil
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCategoryBinding
    private val categoryViewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        setupValidation()
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            saveMBt.setOnClickListener { setupAddCategory() }
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

    private fun setupAddCategory(){
        val categoryName = binding.categoryNameTIET.text.toString()
        categoryViewModel.add(CategoryDto(categoryName))
        setupObserveAddCategory()
    }

    private fun setupObserveAddCategory(){
        val loadingDialogUtil = LoadingDialogUtil(this)
        ObserveResource.setup(
            this,
            categoryViewModel.added,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> setupOnSuccess(message)},
            onFailure = { message -> binding.categoryNameTIL.helperText = message }
        )
    }

    private fun setupOnSuccess(message: String){
        ToastUtil.show(this,message)
        setResult(RESULT_OK)
        finish()
    }
}