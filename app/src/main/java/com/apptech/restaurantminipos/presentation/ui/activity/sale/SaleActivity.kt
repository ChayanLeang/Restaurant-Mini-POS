package com.apptech.restaurantminipos.presentation.ui.activity.sale

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apptech.restaurantminipos.databinding.ActivitySaleBinding
import com.apptech.restaurantminipos.domain.model.Sale
import com.apptech.restaurantminipos.presentation.adapter.SaleAdapter
import com.apptech.restaurantminipos.presentation.viewmodel.SaleViewModel
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.ObserveResource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaleActivity : AppCompatActivity() {
    private lateinit var saleAdapter: SaleAdapter
    private lateinit var binding: ActivitySaleBinding
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private val saleViewModel : SaleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependencies()
        initView()
        setupLoadSales()
    }

    private fun setupDependencies(){
        saleAdapter = SaleAdapter(this)
        loadingDialogUtil = LoadingDialogUtil(this)
    }

    private fun initView(){
        binding.mtb.setNavigationOnClickListener { finish() }
    }

    private fun setupLoadSales(){
        saleViewModel.loadAll()
        ObserveResource.setup(
            this,
            saleViewModel.loadedAll,
            onVisibility = { setupOnVisibility() },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { sales -> setupOnSuccess(sales) },
            onFailure = { _ -> setupOnFailure()}
        )
    }

    private fun setupOnVisibility(){
        loadingDialogUtil.show(false)
        binding.apply {
            noItemTV.visibility = View.GONE
            noItemIV.visibility = View.GONE
            rv.visibility = View.GONE
        }
    }

    private fun setupOnSuccess(sales: List<Sale>){
        if(sales.isEmpty()){
            binding.apply {
                noItemIV.visibility = View.VISIBLE
                noItemTV.visibility = View.VISIBLE
            }
        }
        else{
            saleAdapter.setSales(sales)
            binding.rv.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(this@SaleActivity)
                adapter = saleAdapter
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