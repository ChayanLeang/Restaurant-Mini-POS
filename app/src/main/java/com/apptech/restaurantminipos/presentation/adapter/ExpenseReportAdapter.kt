package com.apptech.restaurantminipos.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apptech.restaurantminipos.R
import com.apptech.restaurantminipos.databinding.ActivityExpenseItemBinding
import com.apptech.restaurantminipos.domain.model.Expense

class ExpenseReportAdapter(val context: Context) :RecyclerView.Adapter<ExpenseReportAdapter
                                                             .ExpenseReportViewHolder>() {

    private lateinit var expenses: List<Expense>

    fun setExpenses(expenses: List<Expense>){
        this.expenses = expenses
        notifyDataSetChanged()
    }

    inner class ExpenseReportViewHolder(val binding: ActivityExpenseItemBinding) : RecyclerView.ViewHolder(
                                                                                              binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, iewType: Int): ExpenseReportViewHolder {
        val binding = ActivityExpenseItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ExpenseReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseReportViewHolder,position: Int) {
        val expense = expenses[position]
        holder.binding.apply {
            expenseNameTV.text = expense.name
            expenseAmountTV.text = context.getString(R.string.one_string_format,expense.amount)
            dateTimeTV.text = context.getString(R.string.two_string_format,expense.date,expense.time)
            noteTV.text = context.getString(R.string.note_format,expense.note)
            deleteIV.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = expenses.size
}