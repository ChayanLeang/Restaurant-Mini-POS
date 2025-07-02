package com.apptech.restaurantminipos.presentation.ui.activity.order_type

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apptech.restaurantminipos.databinding.ActivityOrderTypeBinding
import com.apptech.restaurantminipos.domain.model.OrderType
import com.apptech.restaurantminipos.presentation.adapter.OrderTypeAdapter
import com.apptech.restaurantminipos.presentation.viewmodel.OrderTypeViewModel
import com.apptech.restaurantminipos.util.DialogUtil
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.NavigationUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.ResultLauncherUtil
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderTypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderTypeBinding
    private lateinit var orderTypeAdapter: OrderTypeAdapter
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private val orderTypeViewModel : OrderTypeViewModel by viewModels()
    private lateinit var addResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependencies()
        initView()
        setupLoadOrderTypes()
    }

    private fun setupDependencies(){
        loadingDialogUtil = LoadingDialogUtil(this)
        addResultLauncher = ResultLauncherUtil.setup(this) { orderTypeViewModel.loadItems() }
        editResultLauncher = ResultLauncherUtil.setup(this) { orderTypeViewModel.loadItems() }
        orderTypeAdapter = OrderTypeAdapter(this,editResultLauncher){ code -> setupDeleteOrderType(code)}
    }

    private fun setupDeleteOrderType(code: Int){
        orderTypeViewModel.deleteItem(code)
        ObserveResource.setup(
            this,
            orderTypeViewModel.deleted,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> ToastUtil.show(this,message)},
            onFailure = { message -> setupOnDeleteOrderTypeFailure(message)}
        )
    }

    private fun setupOnDeleteOrderTypeFailure(message: String){
        val dialogUtil = DialogUtil(this)
        dialogUtil.showErrorDialog(message)
    }

    private fun initView(){
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            gotoAddIV.setOnClickListener { NavigationUtil.navigateTo<AddOrderTypeActivity>(
                                           this@OrderTypeActivity,null,addResultLauncher) }
        }
    }

    private fun setupLoadOrderTypes(){
        orderTypeViewModel.loadItems()
        ObserveResource.setup(
            this,
            orderTypeViewModel.loadedAll,
            onVisibility = { setupOnVisibility() },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { orderTypes -> setupOnSuccess(orderTypes) },
            onFailure = { _ -> setupOnLoadOrderTypesFailure() }
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

    private fun setupOnSuccess(orderTypes: List<OrderType>){
        if(orderTypes.isEmpty()){
            binding.apply {
                noItemIV.visibility = View.VISIBLE
                noItemTV.visibility = View.VISIBLE
            }
        }
        else{
            orderTypeAdapter.setOrderTypes(orderTypes)
            binding.rv.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(this@OrderTypeActivity)
                adapter = orderTypeAdapter
            }
        }
    }

    private fun setupOnLoadOrderTypesFailure(){
        binding.apply {
            noItemIV.visibility = View.VISIBLE
            noItemTV.visibility = View.VISIBLE
        }
    }
}