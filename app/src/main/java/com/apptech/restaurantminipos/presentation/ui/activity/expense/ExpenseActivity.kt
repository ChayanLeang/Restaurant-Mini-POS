package com.apptech.restaurantminipos.presentation.ui.activity.expense

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apptech.restaurantminipos.databinding.ActivityExpenseBinding
import com.apptech.restaurantminipos.domain.model.Expense
import com.apptech.restaurantminipos.presentation.adapter.ExpenseAdapter
import com.apptech.restaurantminipos.presentation.viewmodel.ExpenseViewModel
import com.apptech.restaurantminipos.util.DialogUtil
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.NavigationUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.ResultLauncherUtil
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpenseActivity : AppCompatActivity() {
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var binding : ActivityExpenseBinding
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private val expenseViewModel : ExpenseViewModel by viewModels()
    private lateinit var addResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependencies()
        initView()
        setupLoadExpenses()
    }

    private fun setupDependencies(){
        loadingDialogUtil = LoadingDialogUtil(this)
        addResultLauncher = ResultLauncherUtil.setup(this) { expenseViewModel.loadAll() }
        editResultLauncher = ResultLauncherUtil.setup(this) { expenseViewModel.loadAll() }
        expenseAdapter = ExpenseAdapter(this,editResultLauncher){ code ->  setupDeleteExpense(code)}
    }

    private fun setupDeleteExpense(code: Int){
        expenseViewModel.delete(code)
        ObserveResource.setup(
            this,
            expenseViewModel.deleted,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> ToastUtil.show(this,message) },
            onFailure = { message ->  setupOnDeleteExpenseFailure(message)}
        )
    }

    private fun setupOnDeleteExpenseFailure(message: String){
        val dialogUtil = DialogUtil(this)
        dialogUtil.showErrorDialog(message)
    }

    private fun initView(){
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            searchTIL.setEndIconOnClickListener { searchTIET.setText(null) }
            gotoAddIV.setOnClickListener { NavigationUtil.navigateTo<AddExpenseActivity>(
                                           this@ExpenseActivity,null,addResultLauncher) }
        }
    }

    private fun setupLoadExpenses(){
        expenseViewModel.loadAll()
        ObserveResource.setup(
            this,
            expenseViewModel.loadedAll,
            onVisibility = { setupOnVisibility() },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { expenses -> setupOnSuccess(expenses) },
            onFailure = { _ -> setupOnLoadExpensesFailure() }
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

    private fun setupOnSuccess(expenses: List<Expense>){
        if(expenses.isEmpty()){
            binding.apply {
                noItemIV.visibility = View.VISIBLE
                noItemTV.visibility = View.VISIBLE
            }
        }
        else{
            expenseAdapter.setExpenses(expenses)
            binding.rv.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(this@ExpenseActivity)
                adapter = expenseAdapter
            }
        }
    }

    private fun setupOnLoadExpensesFailure(){
        binding.apply {
            noItemIV.visibility = View.VISIBLE
            noItemTV.visibility = View.VISIBLE
        }
    }
}