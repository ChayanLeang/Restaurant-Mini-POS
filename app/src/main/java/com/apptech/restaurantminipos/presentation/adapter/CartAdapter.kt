package com.apptech.restaurantminipos.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apptech.restaurantminipos.R
import com.apptech.restaurantminipos.databinding.ActivityCartItemBinding
import com.apptech.restaurantminipos.domain.model.Cart
import com.squareup.picasso.Picasso

class CartAdapter(val context: Context,val onClicked:(Int) -> Unit) : RecyclerView.Adapter<CartAdapter
                                                                                  .CartViewHolder>() {
    private lateinit var carts: List<Cart>

    fun setCarts(carts: List<Cart>){
        this.carts = carts
        notifyDataSetChanged()
    }

    inner class CartViewHolder(val binding: ActivityCartItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, iewType: Int): CartViewHolder {
        val binding = ActivityCartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder,position: Int) {
        val cart = carts[position]
        holder.binding.apply {
            Picasso.get().load(cart.product.imageUrl).into(productIV)
            productNameTV.text = cart.product.name
            productSellPriceTV.text = context.getString(R.string.one_string_format,cart.product.sellPrice)
            quantityTV.text = cart.quantity.toString()
            deleteIV.setOnClickListener { onClicked(cart.code) }
        }
    }

    override fun getItemCount(): Int = carts.size
}