package com.apptech.restaurantminipos.presentation.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apptech.restaurantminipos.R
import com.apptech.restaurantminipos.databinding.ActivityCartBinding
import com.apptech.restaurantminipos.domain.model.Cart
import com.apptech.restaurantminipos.presentation.adapter.CartAdapter
import com.apptech.restaurantminipos.presentation.ui.fragment.PaymentDialogFragment
import com.apptech.restaurantminipos.presentation.viewmodel.CartViewModel
import com.apptech.restaurantminipos.util.DialogUtil
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartActivity : AppCompatActivity() {
    private lateinit var cartAdapter: CartAdapter
    private lateinit var binding: ActivityCartBinding
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private val cartViewModel : CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependencies()
        initView()
        setupLoadCarts()
    }

    private fun setupDependencies(){
        loadingDialogUtil = LoadingDialogUtil(this)
        cartAdapter = CartAdapter(this){ code -> setupDeleteCart(code) }
    }

    private fun setupDeleteCart(code: Int){
        cartViewModel.delete(code)
        ObserveResource.setup(
            this,
            cartViewModel.deleted,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> ToastUtil.show(this,message) },
            onFailure = { message -> setupOnDeleteCartFailure(message) }
        )
    }

    private fun setupOnDeleteCartFailure(message: String){
        val dialogUtil = DialogUtil(this)
        dialogUtil.showErrorDialog(message)
    }

    private fun initView(){
        binding.mtb.setNavigationOnClickListener { finish() }
    }

    private fun setupLoadCarts(){
        cartViewModel.loadAll()
        ObserveResource.setup(
            this,
            cartViewModel.loadedAll,
            onVisibility = { setupOnVisibility() },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { carts -> setupOnSuccess(carts) },
            onFailure = { _ -> setupOnFailure() }
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

    private fun setupOnSuccess(carts: List<Cart>){
        if(carts.isEmpty()){
            binding.apply {
                noItemIV.visibility = View.VISIBLE
                noItemTV.visibility = View.VISIBLE
            }
        }
        else{
            cartAdapter.setCarts(carts)
            binding.rv.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(this@CartActivity)
                adapter = cartAdapter
            }
        }
        setupLoadTotalPrice()
        binding.paymentMBt.setOnClickListener { setupLoadPaymentDialog() }
    }

    private fun setupLoadTotalPrice(){
        cartViewModel.loadTotalPrice()
        cartViewModel.totalPrice.observe(this) { totalPrice -> binding.totalPriceTV.text = getString(R
                                                              .string.total_price_format,totalPrice) }
    }

    private fun setupLoadPaymentDialog(){
        val paymentDialog = PaymentDialogFragment()
        paymentDialog.show(supportFragmentManager,"Payment Dialog")
    }

    private fun setupOnFailure(){
        binding.apply {
            noItemIV.visibility = View.VISIBLE
            noItemTV.visibility = View.VISIBLE
        }
    }
}