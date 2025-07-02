package com.apptech.restaurantminipos.presentation.ui.activity.expense

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apptech.restaurantminipos.R
import com.apptech.restaurantminipos.databinding.ActivityExpenseReportBinding
import com.apptech.restaurantminipos.domain.model.Expense
import com.apptech.restaurantminipos.presentation.adapter.ExpenseReportAdapter
import com.apptech.restaurantminipos.presentation.viewmodel.ExpenseViewModel
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.ObserveResource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpenseReportActivity : AppCompatActivity() {
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private lateinit var binding : ActivityExpenseReportBinding
    private val expenseViewModel : ExpenseViewModel by viewModels()
    private lateinit var expenseReportAdapter: ExpenseReportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependencies()
        initView()
        setupLoadExpenseReports()
    }

    private fun setupDependencies(){
        loadingDialogUtil = LoadingDialogUtil(this)
        expenseReportAdapter = ExpenseReportAdapter(this)
    }

    private fun initView(){
        binding.mtb.setNavigationOnClickListener { finish() }
    }

    private fun setupLoadExpenseReports(){
        expenseViewModel.loadAll()
        ObserveResource.setup(
            this,
            expenseViewModel.loadedAll,
            onVisibility = { setupOnVisibility() },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { expenses -> setupOnSuccess(expenses) },
            onFailure = {}
        )
    }

    private fun setupOnVisibility(){
        loadingDialogUtil.show(false)
        binding.apply {
            rv.visibility = View.GONE
            noItemIV.visibility = View.GONE
            noItemTV.visibility = View.GONE
        }
        setupLoadTotalExpense()
    }

    private fun setupOnSuccess(expenses: List<Expense>){
        if(expenses.isEmpty()){
            binding.apply {
                noItemIV.visibility = View.VISIBLE
                noItemTV.visibility = View.VISIBLE
            }
        }
        else{
            expenseReportAdapter.setExpenses(expenses)
            binding.rv.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(this@ExpenseReportActivity)
                adapter = expenseReportAdapter
            }
        }
    }

    private fun setupLoadTotalExpense(){
        expenseViewModel.loadTotalExpense()
        expenseViewModel.totalExpense.observe(this) { totalExpense -> binding.totalExpenseTV.text =
                                            getString(R.string.total_expense_format,totalExpense) }
    }
}