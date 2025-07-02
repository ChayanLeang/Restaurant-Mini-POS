package com.apptech.restaurantminipos.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apptech.restaurantminipos.databinding.ActivityReportBinding
import com.apptech.restaurantminipos.presentation.ui.activity.expense.ExpenseReportActivity
import com.apptech.restaurantminipos.presentation.ui.activity.sale.SaleActivity
import com.apptech.restaurantminipos.util.NavigationUtil

class ReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            allSales.setOnClickListener { NavigationUtil.navigateTo<SaleActivity>(this@ReportActivity,
                                                                                          null,null) }
            allExpenses.setOnClickListener { NavigationUtil.navigateTo<ExpenseReportActivity>(
                                                              this@ReportActivity,null,null) }
        }
    }
}