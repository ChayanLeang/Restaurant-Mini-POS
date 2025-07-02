package com.apptech.restaurantminipos.presentation.ui.activity.category

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apptech.restaurantminipos.databinding.ActivityCategoryBinding
import com.apptech.restaurantminipos.domain.model.Category
import com.apptech.restaurantminipos.presentation.adapter.CategoryAdapter
import com.apptech.restaurantminipos.presentation.viewmodel.CategoryViewModel
import com.apptech.restaurantminipos.util.DialogUtil
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.NavigationUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.ResultLauncherUtil
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var binding: ActivityCategoryBinding
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private val categoryViewModel : CategoryViewModel by viewModels()
    private lateinit var addResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependencies()
        initView()
        setupLoadCategories()
    }

    private fun setupDependencies(){
        loadingDialogUtil = LoadingDialogUtil(this)
        addResultLauncher = ResultLauncherUtil.setup(this) { categoryViewModel.loadAll() }
        editResultLauncher = ResultLauncherUtil.setup(this) { categoryViewModel.loadAll() }
        categoryAdapter = CategoryAdapter(this,editResultLauncher){ code -> setupDeleteCategory(code)}
    }

    private fun setupDeleteCategory(code: Int){
        categoryViewModel.delete(code)
        ObserveResource.setup(
            this,
            categoryViewModel.deleted,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> ToastUtil.show(this,message)},
            onFailure = { message -> setupOnDeleteCategoryFailure(message)}
        )
    }

    private fun setupOnDeleteCategoryFailure(message: String){
        val dialogUtil = DialogUtil(this)
        dialogUtil.showErrorDialog(message)
    }

    private fun initView(){
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            searchTIL.setEndIconOnClickListener { searchTIET.setText(null) }
            gotoAddIV.setOnClickListener { NavigationUtil.navigateTo<AddCategoryActivity>(
                                           this@CategoryActivity,null,addResultLauncher) }
        }
    }

    private fun setupLoadCategories(){
        categoryViewModel.loadAll()
        ObserveResource.setup(
            this,
            categoryViewModel.loadedAll,
            onVisibility = { setupOnVisibility() },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { categories -> setupOnSuccess(categories) },
            onFailure = { _ -> setupOnLoadCategoriesFailure() }
        )
    }

    private fun setupOnVisibility(){
        loadingDialogUtil.show(false)
        binding.apply {
            noItemIV.visibility = View.GONE
            noItemTV.visibility = View.GONE
            rv.visibility = View.GONE
        }
    }

    private fun setupOnSuccess(categories: List<Category>){
        if(categories.isEmpty()){
            binding.apply {
                noItemIV.visibility = View.VISIBLE
                noItemTV.visibility = View.VISIBLE
            }
        }
        else{
            categoryAdapter.setCategories(categories)
            binding.rv.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(this@CategoryActivity)
                adapter = categoryAdapter
            }
        }
    }

    private fun setupOnLoadCategoriesFailure(){
        binding.apply {
            noItemIV.visibility = View.VISIBLE
            noItemTV.visibility = View.VISIBLE
        }
    }
}