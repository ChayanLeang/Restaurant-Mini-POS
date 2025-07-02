package com.apptech.restaurantminipos.presentation.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.apptech.restaurantminipos.R
import com.apptech.restaurantminipos.databinding.ActivityProductItemBinding
import com.apptech.restaurantminipos.domain.model.Product
import com.apptech.restaurantminipos.presentation.ui.activity.product.EditProductActivity
import com.apptech.restaurantminipos.util.NavigationUtil
import com.squareup.picasso.Picasso

class ProductAdapter(val context: Context, val resultLauncher : ActivityResultLauncher<Intent>,
                     val onClicked: (Int) -> Unit) : RecyclerView.Adapter<ProductAdapter
                                                                .ProductViewHolder>() {
    private lateinit var products: List<Product>

    fun setProducts(products: List<Product>){
        this.products = products
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(val binding: ActivityProductItemBinding) : RecyclerView.ViewHolder(
                                                                                         binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, iewType: Int): ProductViewHolder {
        val binding = ActivityProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder,position: Int) {
        val product = products[position]
        holder.binding.apply {
            Picasso.get().load(product.imageUrl).into(productIV)
            productNameTV.text = product.name
            productSellPriceTV.text = context.getString(R.string.sell_price_format,product.sellPrice)
            deleteIV.setOnClickListener { onClicked(product.code) }
            productItem.setOnClickListener { NavigationUtil.navigateTo<EditProductActivity>(context,
                                    Bundle().apply { putInt("code",product.code) },resultLauncher) }
        }
    }

    override fun getItemCount(): Int = products.size
}