package com.apptech.restaurantminipos.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apptech.restaurantminipos.databinding.ActivitySettingBinding
import com.apptech.restaurantminipos.presentation.ui.activity.category.CategoryActivity
import com.apptech.restaurantminipos.presentation.ui.activity.order_type.OrderTypeActivity
import com.apptech.restaurantminipos.presentation.ui.activity.payment_method.PaymentMethodActivity
import com.apptech.restaurantminipos.util.NavigationUtil

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        binding.apply {
            mtb.setNavigationOnClickListener { finish()}
            allCategories.setOnClickListener { NavigationUtil.navigateTo<CategoryActivity>(
                                                          this@SettingActivity,null,null) }
            allPaymentMethods.setOnClickListener { NavigationUtil.navigateTo<PaymentMethodActivity>(
                                                                    this@SettingActivity,null,null)}
            allOrderTypes.setOnClickListener { NavigationUtil.navigateTo<OrderTypeActivity>(
                                                            this@SettingActivity,null,null)}
        }
    }
}