package com.apptech.restaurantminipos.presentation.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.apptech.restaurantminipos.R
import com.apptech.restaurantminipos.data.dto.CartDto
import com.apptech.restaurantminipos.databinding.ActivityPosBinding
import com.apptech.restaurantminipos.domain.model.Product
import com.apptech.restaurantminipos.presentation.adapter.PosProductAdapter
import com.apptech.restaurantminipos.presentation.viewmodel.CartViewModel
import com.apptech.restaurantminipos.presentation.viewmodel.ProductViewModel
import com.apptech.restaurantminipos.util.DialogUtil
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.NavigationUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPosBinding
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private lateinit var posProductAdapter: PosProductAdapter
    private val cartViewModel : CartViewModel by viewModels()
    private val productViewModel : ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependencies()
        initView()
        setupLoadProducts()
    }

    private fun setupDependencies(){
        loadingDialogUtil = LoadingDialogUtil(this)
        posProductAdapter = PosProductAdapter(this){ code -> setupAddCart(code) }
    }

    private fun setupAddCart(code: Int){
        cartViewModel.add(CartDto(code))
        ObserveResource.setup(
            this,
            cartViewModel.added,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> ToastUtil.show(this,message) },
            onFailure = { message -> setupOnAddCartFailure(message)}
        )
    }

    private fun setupOnAddCartFailure(message: String){
        val dialogUtil = DialogUtil(this)
        dialogUtil.showErrorDialog(message)
    }

    private fun initView(){
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            searchTIL.setEndIconOnClickListener { searchTIET.setText(null) }
            mtb.setOnMenuItemClickListener { item ->
                if(item.itemId == R.id.cart){
                    NavigationUtil.navigateTo<CartActivity>(this@PosActivity,null,null)
                }
                true
            }
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
            onFailure = { _ -> setupOnFailure() }
        )
    }

    private fun setupOnVisibility(){
        loadingDialogUtil.show(false)
        binding.apply {
            noItemIV.visibility = View.GONE
            noItemTV.visibility = View.GONE
            gv.visibility = View.GONE
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
            posProductAdapter.setProducts(products)
            binding.gv.apply {
                visibility = View.VISIBLE
                adapter = posProductAdapter
            }
        }
    }

    private fun setupOnFailure(){
        binding.apply {
            noItemIV.visibility = View.VISIBLE
            noItemTV.visibility = View.VISIBLE
        }
    }
}