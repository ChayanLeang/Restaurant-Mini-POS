package com.apptech.restaurantminipos.presentation.ui.activity.product

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apptech.restaurantminipos.databinding.ActivityProductBinding
import com.apptech.restaurantminipos.domain.model.Product
import com.apptech.restaurantminipos.presentation.adapter.ProductAdapter
import com.apptech.restaurantminipos.presentation.viewmodel.ProductViewModel
import com.apptech.restaurantminipos.util.DialogUtil
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.NavigationUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.ResultLauncherUtil
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductActivity : AppCompatActivity() {
    private lateinit var productAdapter: ProductAdapter
    private lateinit var binding: ActivityProductBinding
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private val productViewModel : ProductViewModel by viewModels()
    private lateinit var addResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependencies()
        initView()
        setupLoadProducts()
    }

    private fun setupDependencies(){
        loadingDialogUtil = LoadingDialogUtil(this)
        addResultLauncher = ResultLauncherUtil.setup(this) { productViewModel.loadAll() }
        editResultLauncher = ResultLauncherUtil.setup(this) { productViewModel.loadAll() }
        productAdapter = ProductAdapter(this,editResultLauncher){ code -> setupDeleteProduct(code)}
    }

    private fun setupDeleteProduct(code: Int){
        productViewModel.delete(code)
        ObserveResource.setup(
            this,
            productViewModel.deleted,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> ToastUtil.show(this,message)},
            onFailure = { message -> setupOnDeleteProductFailure(message)}
        )
    }

    private fun setupOnDeleteProductFailure(message: String){
        val dialogUtil = DialogUtil(this)
        dialogUtil.showErrorDialog(message)
    }

    private fun initView(){
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            searchTIL.setEndIconOnClickListener { searchTIET.setText(null) }
            gotoAddIV.setOnClickListener { NavigationUtil.navigateTo<AddProductActivity>(
                                            this@ProductActivity,null,addResultLauncher) }
        }
    }

    private fun setupLoadProducts(){
        productViewModel.loadAll()
        ObserveResource.setup(
            this,
            productViewModel.loadedAll,
            onVisibility = { setupOnVisibility() },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { products -> setupOnSuccess(products) },
            onFailure = { _ -> setupOnLoadProductsFailure() }
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

    private fun setupOnSuccess(products: List<Product>){
        if(products.isEmpty()){
            binding.apply {
                noItemIV.visibility = View.VISIBLE
                noItemTV.visibility = View.VISIBLE
            }
        }
        else{
            productAdapter.setProducts(products)
            binding.rv.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(this@ProductActivity)
                adapter = productAdapter
            }
        }
    }

    private fun setupOnLoadProductsFailure(){
        binding.apply {
            noItemIV.visibility = View.VISIBLE
            noItemTV.visibility = View.VISIBLE
        }
    }
}