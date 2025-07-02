package com.apptech.restaurantminipos.presentation.ui.activity.customer

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apptech.restaurantminipos.databinding.ActivityCustomerBinding
import com.apptech.restaurantminipos.domain.model.Customer
import com.apptech.restaurantminipos.presentation.adapter.CustomerAdapter
import com.apptech.restaurantminipos.presentation.viewmodel.CustomerViewModel
import com.apptech.restaurantminipos.util.DialogUtil
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.NavigationUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.ResultLauncherUtil
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerActivity : AppCompatActivity() {
    private lateinit var customerAdapter: CustomerAdapter
    private lateinit var binding : ActivityCustomerBinding
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private val customerViewModel : CustomerViewModel by viewModels()
    private lateinit var addResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependencies()
        initView()
        setupLoadCustomers()
    }

    private fun setupDependencies(){
        loadingDialogUtil = LoadingDialogUtil(this)
        addResultLauncher = ResultLauncherUtil.setup(this) { customerViewModel.loadAll() }
        editResultLauncher = ResultLauncherUtil.setup(this) { customerViewModel.loadAll() }
        customerAdapter = CustomerAdapter(this,editResultLauncher){ code ->  setupDeleteCustomer(code)}
    }

    private fun setupDeleteCustomer(code: Int){
        customerViewModel.delete(code)
        ObserveResource.setup(
            this,
            customerViewModel.deleted,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> ToastUtil.show(this,message) },
            onFailure = { message ->  setupOnDeleteCustomerFailure(message)}
        )
    }

    private fun setupOnDeleteCustomerFailure(message: String){
        val dialogUtil = DialogUtil(this)
        dialogUtil.showErrorDialog(message)
    }

    private fun initView(){
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            searchTIL.setEndIconOnClickListener { searchTIET.setText(null) }
            gotoAddIV.setOnClickListener { NavigationUtil.navigateTo<AddCustomerActivity>(
                                           this@CustomerActivity,null,addResultLauncher) }
        }
    }

    private fun setupLoadCustomers(){
        customerViewModel.loadAll()
        ObserveResource.setup(
            this,
            customerViewModel.loadedAll,
            onVisibility = { setupOnVisibility() },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { customers -> setupOnSuccess(customers) },
            onFailure = { _ -> setupOnLoadCustomersFailure() }
        )
    }

    private fun setupOnVisibility(){
        loadingDialogUtil.show(false)
        binding.apply {
            rv.visibility = View.GONE
            noItemIV.visibility = View.GONE
            noItemTV.visibility = View.GONE
        }
    }

    private fun setupOnSuccess(customers: List<Customer>){
        if(customers.isEmpty()){
            binding.apply {
                noItemIV.visibility = View.VISIBLE
                noItemTV.visibility = View.VISIBLE
            }
        }
        else{
            customerAdapter.setCustomers(customers)
            binding.rv.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(this@CustomerActivity)
                adapter = customerAdapter
            }
        }
    }

    private fun setupOnLoadCustomersFailure(){
        binding.apply {
            noItemIV.visibility = View.VISIBLE
            noItemTV.visibility = View.VISIBLE
        }
    }
}