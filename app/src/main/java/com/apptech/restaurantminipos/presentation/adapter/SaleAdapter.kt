package com.apptech.restaurantminipos.presentation.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apptech.restaurantminipos.R
import com.apptech.restaurantminipos.databinding.ActivitySaleItemBinding
import com.apptech.restaurantminipos.domain.model.Sale
import com.apptech.restaurantminipos.presentation.ui.activity.sale.SaleDetailActivity
import com.apptech.restaurantminipos.util.NavigationUtil

class SaleAdapter(val context: Context) : RecyclerView.Adapter<SaleAdapter.SaleViewHolder>() {

    private lateinit var sales: List<Sale>

    fun setSales(sales: List<Sale>){
        this.sales = sales
        notifyDataSetChanged()
    }

    inner class SaleViewHolder(val binding: ActivitySaleItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, iewType: Int): SaleViewHolder {
        val binding = ActivitySaleItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SaleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SaleViewHolder,position: Int) {
        val sale = sales[position]
        holder.binding.apply {
            customerNameTV.text = sale.customer.name
            codeTV.text = context.getString(R.string.code_format,sale.code)
            orderTypeTV.text = context.getString(R.string.order_type_format,sale.orderType.name)
            paymentMethodTV.text = context.getString(R.string.payment_method_format,sale.paymentMethod.name)
            dateTimeTV.text = sale.dateTime
            tableNumberTV.text = context.getString(R.string.table_number_format,sale.tableNumber)
            saleItem.setOnClickListener { NavigationUtil.navigateTo<SaleDetailActivity>(context,
                                             Bundle().apply { putInt("code",sale.code) },null) }
        }
    }

    override fun getItemCount(): Int = sales.size
}