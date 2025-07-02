package com.apptech.restaurantminipos.presentation.ui.activity.payment_method

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apptech.restaurantminipos.databinding.ActivityPaymentMethodBinding
import com.apptech.restaurantminipos.domain.model.PaymentMethod
import com.apptech.restaurantminipos.presentation.adapter.PaymentMethodAdapter
import com.apptech.restaurantminipos.presentation.viewmodel.PaymentMethodViewModel
import com.apptech.restaurantminipos.util.DialogUtil
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.NavigationUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.ResultLauncherUtil
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentMethodActivity : AppCompatActivity() {
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private lateinit var binding: ActivityPaymentMethodBinding
    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private lateinit var addResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editResultLauncher: ActivityResultLauncher<Intent>
    private val paymentMethodViewModel : PaymentMethodViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependencies()
        initView()
        setupLoadPaymentMethods()
    }

    private fun setupDependencies(){
        loadingDialogUtil = LoadingDialogUtil(this)
        addResultLauncher = ResultLauncherUtil.setup(this) { paymentMethodViewModel.loadAll() }
        editResultLauncher = ResultLauncherUtil.setup(this) { paymentMethodViewModel.loadAll() }
        paymentMethodAdapter = PaymentMethodAdapter(this,editResultLauncher){ code ->
                                                      setupDeletePaymentMethod(code)}
    }

    private fun setupDeletePaymentMethod(code: Int){
        paymentMethodViewModel.delete(code)
        ObserveResource.setup(
            this,
            paymentMethodViewModel.deleted,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> ToastUtil.show(this,message)},
            onFailure = { message -> setupOnDeletePaymentMethodFailure(message)}
        )
    }

    private fun setupOnDeletePaymentMethodFailure(message: String){
        val dialogUtil = DialogUtil(this)
        dialogUtil.showErrorDialog(message)
    }

    private fun initView(){
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            gotoAddIV.setOnClickListener { NavigationUtil.navigateTo<AddPaymentMethodActivity>(
                                           this@PaymentMethodActivity,null,addResultLauncher) }
        }
    }

    private fun setupLoadPaymentMethods(){
        paymentMethodViewModel.loadAll()
        ObserveResource.setup(
            this,
            paymentMethodViewModel.loadedAll,
            onVisibility = { setupOnVisibility() },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { paymentMethods -> setupOnSuccess(paymentMethods) },
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

    private fun setupOnSuccess(paymentMethods: List<PaymentMethod>){
        if(paymentMethods.isEmpty()){
            binding.apply {
                noItemIV.visibility = View.VISIBLE
                noItemTV.visibility = View.VISIBLE
            }
        }
        else{
            paymentMethodAdapter.setPaymentMethods(paymentMethods)
            binding.rv.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(this@PaymentMethodActivity)
                adapter = paymentMethodAdapter
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