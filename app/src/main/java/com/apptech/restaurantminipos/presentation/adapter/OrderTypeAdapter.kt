package com.apptech.restaurantminipos.presentation.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.apptech.restaurantminipos.databinding.ActivityOrderTypeItemBinding
import com.apptech.restaurantminipos.domain.model.OrderType
import com.apptech.restaurantminipos.presentation.ui.activity.order_type.EditOrderTypeActivity
import com.apptech.restaurantminipos.util.NavigationUtil

class OrderTypeAdapter(val context: Context,val resultLauncher : ActivityResultLauncher<Intent>,
                       val onClicked: (Int) -> Unit) : RecyclerView.Adapter<OrderTypeAdapter
                                                                   .OrderTypeViewHolder>() {
    private lateinit var orderTypes: List<OrderType>

    fun setOrderTypes(orderTypes: List<OrderType>){
        this.orderTypes = orderTypes
        notifyDataSetChanged()
    }

    inner class OrderTypeViewHolder(val binding: ActivityOrderTypeItemBinding) : RecyclerView.ViewHolder(
                                                                                            binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, iewType: Int): OrderTypeViewHolder {
        val binding = ActivityOrderTypeItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderTypeViewHolder,position: Int) {
        val orderType = orderTypes[position]
        holder.binding.apply {
            orderTypeNameTV.text = orderType.name
            deleteIV.setOnClickListener { onClicked(orderType.code) }
            orderTypeItem.setOnClickListener { NavigationUtil.navigateTo< EditOrderTypeActivity>(context,
                                       Bundle().apply { putInt("code",orderType.code) },resultLauncher) }
        }
    }

    override fun getItemCount(): Int = orderTypes.size
}