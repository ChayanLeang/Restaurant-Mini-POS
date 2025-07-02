package com.apptech.restaurantminipos.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.apptech.restaurantminipos.R
import com.apptech.restaurantminipos.databinding.ActivityPosProductItemBinding
import com.apptech.restaurantminipos.domain.model.Product
import com.squareup.picasso.Picasso

class PosProductAdapter(val context: Context,val onClicked:(Int) -> Unit) : BaseAdapter() {
    private lateinit var products: List<Product>

    fun setProducts(products: List<Product>){
        this.products = products
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return products.size
    }

    override fun getItem(position: Int): Product {
        return products[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ActivityPosProductItemBinding = if (convertView == null) {
            ActivityPosProductItemBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            ActivityPosProductItemBinding.bind(convertView)
        }

        val product = getItem(position)
        binding.apply {
            Picasso.get().load(product.imageUrl).into(productIV)
            productNameTV.text = product.name
            productSellPriceTV.text = context.getString(R.string.one_string_format,product.sellPrice)
            addToCartTV.setOnClickListener { onClicked(product.code) }
        }

        return binding.root
    }
}