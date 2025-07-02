package com.apptech.restaurantminipos.presentation.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.apptech.restaurantminipos.databinding.ActivityCategoryItemBinding
import com.apptech.restaurantminipos.domain.model.Category
import com.apptech.restaurantminipos.presentation.ui.activity.category.EditCategoryActivity
import com.apptech.restaurantminipos.util.NavigationUtil

class CategoryAdapter(val context: Context, val resultLauncher : ActivityResultLauncher<Intent>,
                      val onClicked: (Int) -> Unit) : RecyclerView.Adapter<CategoryAdapter
                                                                  .CategoryViewHolder>() {
    private lateinit var categories: List<Category>

    fun setCategories(categories: List<Category>){
        this.categories = categories
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(val binding: ActivityCategoryItemBinding) : RecyclerView.ViewHolder(
                                                                                         binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, iewType: Int): CategoryViewHolder {
        val binding = ActivityCategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder,position: Int) {
        val category = categories[position]
        holder.binding.apply {
            categoryNameTV.text = category.name
            deleteIV.setOnClickListener { onClicked(category.code) }
            categoryItem.setOnClickListener { NavigationUtil.navigateTo< EditCategoryActivity>(context,
                                      Bundle().apply { putInt("code",category.code) },resultLauncher) }
        }
    }

    override fun getItemCount(): Int = categories.size
}