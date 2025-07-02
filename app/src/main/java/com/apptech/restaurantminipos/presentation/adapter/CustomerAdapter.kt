package com.apptech.restaurantminipos.presentation.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.apptech.restaurantminipos.databinding.ActivityCustomerItemBinding
import com.apptech.restaurantminipos.domain.model.Customer
import com.apptech.restaurantminipos.presentation.ui.activity.customer.EditCustomerActivity
import com.apptech.restaurantminipos.util.NavigationUtil

class CustomerAdapter(val context: Context,val resultLauncher : ActivityResultLauncher<Intent>,
                      val onClicked: (Int) -> Unit) : RecyclerView.Adapter<CustomerAdapter
                                                                  .CustomerViewHolder>() {
    private lateinit var customers: List<Customer>

    fun setCustomers(customers: List<Customer>){
        this.customers = customers
        notifyDataSetChanged()
    }

    inner class CustomerViewHolder(val binding: ActivityCustomerItemBinding) : RecyclerView.ViewHolder(
                                                                                          binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, iewType: Int): CustomerViewHolder {
        val binding = ActivityCustomerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CustomerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder,position: Int) {
        val customer = customers[position]
        holder.binding.apply {
            customerNameTV.text = customer.name
            phoneNumberTV.text = customer.phoneNumber
            addressTV.text = customer.address
            deleteIV.setOnClickListener { onClicked(customer.code) }
            customerItem.setOnClickListener { NavigationUtil.navigateTo<EditCustomerActivity>(context,
                                     Bundle().apply { putInt("code",customer.code) },resultLauncher) }
        }
    }

    override fun getItemCount(): Int = customers.size
}