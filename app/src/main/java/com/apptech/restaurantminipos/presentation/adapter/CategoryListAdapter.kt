package com.apptech.restaurantminipos.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apptech.restaurantminipos.databinding.FragmentCategoryListItemBinding
import com.apptech.restaurantminipos.domain.model.Category

class CategoryListAdapter(val context: Context, val onClicked: (Int,String) -> Unit) : RecyclerView
                                    .Adapter<CategoryListAdapter.CategoryListViewHolder>() {

    private lateinit var categories: List<Category>

    fun setCategories(categories: List<Category>){
        this.categories = categories
        notifyDataSetChanged()
    }

    inner class CategoryListViewHolder(val binding: FragmentCategoryListItemBinding) : RecyclerView
                                                                          .ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, iewType: Int): CategoryListViewHolder {
        val binding = FragmentCategoryListItemBinding.inflate(LayoutInflater.from(parent.context),parent,
                                                                                                   false)
        return CategoryListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder,position: Int) {
        val category = categories[position]
        holder.binding.apply {
            categoryNameTV.text = category.name
            categoryListItem.setOnClickListener { onClicked(category.code,category.name) }
        }
    }

    override fun getItemCount(): Int = categories.size
}