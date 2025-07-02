package com.apptech.restaurantminipos.presentation.ui.activity.sale

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apptech.restaurantminipos.R
import com.apptech.restaurantminipos.databinding.ActivitySaleDetailBinding
import com.apptech.restaurantminipos.domain.model.SaleDetail
import com.apptech.restaurantminipos.presentation.adapter.SaleDetailAdapter
import com.apptech.restaurantminipos.presentation.viewmodel.SaleDetailViewModel
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.ObserveResource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaleDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySaleDetailBinding
    private lateinit var saleDetailAdapter: SaleDetailAdapter
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private val saleDetailViewModel : SaleDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependencies()
        initView()
        setupLoadSaleDetailBySaleCode()
    }

    private fun setupDependencies(){
        saleDetailAdapter = SaleDetailAdapter(this)
        loadingDialogUtil = LoadingDialogUtil(this)
    }

    private fun initView(){
        binding.mtb.setNavigationOnClickListener { finish() }
    }

    private fun setupLoadSaleDetailBySaleCode(){
        val saleCode = intent.getIntExtra("code",0)
        saleDetailViewModel.loadBySaleCode(saleCode)
        ObserveResource.setup(
            this,
            saleDetailViewModel.loadedAll,
            onVisibility = { setupOnVisibility() },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { sales -> setupOnSuccess(saleCode,sales) },
            onFailure = {}
        )
    }

    private fun setupOnVisibility(){
        loadingDialogUtil.show(false)
        binding.rv.visibility = View.GONE
    }

    private fun setupOnSuccess(saleCode:Int,saleDetails: List<SaleDetail>){
        saleDetailAdapter.setSaleDetails(saleDetails)
        binding.rv.apply {
            visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(this@SaleDetailActivity)
            adapter = saleDetailAdapter
        }
        setupLoadSummary(saleCode)
    }

    private fun setupLoadSummary(saleCode: Int){
        saleDetailViewModel.loadSummary(saleCode)
        saleDetailViewModel.summary.observe(this) { summary ->
            binding.apply {
                subTotalTV.text = getString(R.string.sub_total_format,summary.subTotal)
                discountTV.text = getString(R.string.discount_format,summary.discount)
                totalTV.text = getString(R.string.total_format,summary.totalPrice)
            }
        }
    }
}