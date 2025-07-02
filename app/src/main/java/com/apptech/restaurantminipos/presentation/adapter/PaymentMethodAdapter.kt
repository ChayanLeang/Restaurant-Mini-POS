package com.apptech.restaurantminipos.presentation.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.apptech.restaurantminipos.databinding.ActivityPaymentMethodItemBinding
import com.apptech.restaurantminipos.domain.model.PaymentMethod
import com.apptech.restaurantminipos.presentation.ui.activity.payment_method.EditPaymentMethodActivity
import com.apptech.restaurantminipos.util.NavigationUtil

class PaymentMethodAdapter(val context: Context,val resultLauncher : ActivityResultLauncher<Intent>,
                           val onClicked: (Int) -> Unit) : RecyclerView.Adapter<PaymentMethodAdapter
                                                                       .PaymentMethodViewHolder>() {
    private lateinit var paymentMethods: List<PaymentMethod>

    fun setPaymentMethods(paymentMethods: List<PaymentMethod>){
        this.paymentMethods = paymentMethods
        notifyDataSetChanged()
    }

    inner class PaymentMethodViewHolder(val binding: ActivityPaymentMethodItemBinding) : RecyclerView
                                                                            .ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, iewType: Int): PaymentMethodViewHolder {
        val binding = ActivityPaymentMethodItemBinding.inflate(LayoutInflater.from(parent.context),parent,
                                                                                                    false)
        return PaymentMethodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentMethodViewHolder,position: Int) {
        val paymentMethod = paymentMethods[position]
        holder.binding.apply {
            paymentMethodNameTV.text = paymentMethod.name
            deleteIV.setOnClickListener { onClicked(paymentMethod.code) }
            paymentMethodItem.setOnClickListener { NavigationUtil.navigateTo<EditPaymentMethodActivity>(
                          context,Bundle().apply { putInt("code",paymentMethod.code) },resultLauncher) }
        }
    }

    override fun getItemCount(): Int = paymentMethods.size
}