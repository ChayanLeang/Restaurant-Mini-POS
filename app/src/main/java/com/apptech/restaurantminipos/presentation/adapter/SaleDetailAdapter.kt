package com.apptech.restaurantminipos.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apptech.restaurantminipos.R
import com.apptech.restaurantminipos.databinding.ActivitySaleDetailItemBinding
import com.apptech.restaurantminipos.domain.model.SaleDetail
import com.squareup.picasso.Picasso

class SaleDetailAdapter(val context: Context) : RecyclerView.Adapter<SaleDetailAdapter
                                                            .SaleDetailViewHolder>() {

    private lateinit var saleDetails: List<SaleDetail>

    fun setSaleDetails(saleDetails: List<SaleDetail>){
        this.saleDetails = saleDetails
        notifyDataSetChanged()
    }

    inner class SaleDetailViewHolder(val binding: ActivitySaleDetailItemBinding) : RecyclerView
                                                                      .ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, iewType: Int): SaleDetailViewHolder {
        val binding = ActivitySaleDetailItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SaleDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SaleDetailViewHolder,position: Int) {
        val saleDetail = saleDetails[position]
        holder.binding.apply {
            Picasso.get().load(saleDetail.product.imageUrl).into(productIV)
            productNameTV.text = saleDetail.product.name
            quantityTV.text = context.getString(R.string.quantity_format,saleDetail.quantity)
            totalPriceTV.text = context.getString(R.string.three_string_format,saleDetail.product.sellPrice,
                                                                      saleDetail.quantity,saleDetail.amount)
        }
    }

    override fun getItemCount(): Int = saleDetails.size
}