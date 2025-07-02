package com.apptech.restaurantminipos.presentation.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.apptech.restaurantminipos.R
import com.apptech.restaurantminipos.databinding.ActivityExpenseItemBinding
import com.apptech.restaurantminipos.domain.model.Expense
import com.apptech.restaurantminipos.presentation.ui.activity.expense.EditExpenseActivity
import com.apptech.restaurantminipos.util.NavigationUtil

class ExpenseAdapter(val context: Context,
                     val resultLauncher : ActivityResultLauncher<Intent>,
                     val onClicked: (Int) -> Unit) :RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    private lateinit var expenses: List<Expense>

    fun setExpenses(expenses: List<Expense>){
        this.expenses = expenses
        notifyDataSetChanged()
    }

    inner class ExpenseViewHolder(val binding: ActivityExpenseItemBinding) : RecyclerView.ViewHolder(
                                                                                        binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, iewType: Int): ExpenseViewHolder {
        val binding = ActivityExpenseItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder,position: Int) {
        val expense = expenses[position]
        holder.binding.apply {
            expenseNameTV.text = expense.name
            expenseAmountTV.text = context.getString(R.string.one_string_format,expense.amount)
            dateTimeTV.text = context.getString(R.string.two_string_format,expense.date,expense.time)
            noteTV.text = context.getString(R.string.note_format,expense.note)
            deleteIV.setOnClickListener { onClicked(expense.code) }
            expenseItem.setOnClickListener { NavigationUtil.navigateTo<EditExpenseActivity>(context,
                                    Bundle().apply { putInt("code",expense.code) },resultLauncher) }
        }
    }

    override fun getItemCount(): Int = expenses.size
}