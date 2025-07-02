package com.apptech.restaurantminipos.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apptech.restaurantminipos.databinding.ActivityMainBinding
import com.apptech.restaurantminipos.presentation.ui.activity.customer.CustomerActivity
import com.apptech.restaurantminipos.presentation.ui.activity.expense.ExpenseActivity
import com.apptech.restaurantminipos.presentation.ui.activity.product.ProductActivity
import com.apptech.restaurantminipos.util.NavigationUtil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        binding.apply {
            allCustomers.setOnClickListener { NavigationUtil.navigateTo<CustomerActivity>(
                                                                      this@MainActivity) }
            allProducts.setOnClickListener { NavigationUtil.navigateTo<ProductActivity>(
                                                                    this@MainActivity) }
            pos.setOnClickListener { NavigationUtil.navigateTo<PosActivity>(this@MainActivity) }
            allExpenses.setOnClickListener { NavigationUtil.navigateTo<ExpenseActivity>(
                                                                    this@MainActivity) }
            report.setOnClickListener { NavigationUtil.navigateTo<ReportActivity>(this@MainActivity) }
            setting.setOnClickListener { NavigationUtil.navigateTo<SettingActivity>(this@MainActivity) }
        }
    }
}