package com.apptech.restaurantminipos.presentation.ui.activity.expense

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.apptech.restaurantminipos.R
import com.apptech.restaurantminipos.data.dto.ExpenseDto
import com.apptech.restaurantminipos.databinding.ActivityEditExpenseBinding
import com.apptech.restaurantminipos.domain.model.Expense
import com.apptech.restaurantminipos.presentation.viewmodel.ExpenseViewModel
import com.apptech.restaurantminipos.util.DatePickerDialogUtil
import com.apptech.restaurantminipos.util.DialogUtil
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.TextWatcherUtil
import com.apptech.restaurantminipos.util.TimePickerDialogUtil
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditExpenseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditExpenseBinding
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private val expenseViewModel : ExpenseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependency()
        setupLoadExpenseByCode()
    }

    private fun setupDependency(){
        loadingDialogUtil = LoadingDialogUtil(this)
    }

    private fun setupLoadExpenseByCode(){
        val code = intent.getIntExtra("code",0)
        expenseViewModel.loadByCode(code)
        ObserveResource.setup(
            this,
            expenseViewModel.loadedByCode,
            onVisibility = { setupOnVisibility() },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { expense -> setupOnLoadExpenseByCodeSuccess(expense) },
            onFailure = { message -> setupOnFailure(message)}
        )
    }

    private fun setupOnVisibility(){
        loadingDialogUtil.show(false)
        binding.sv.visibility = View.GONE
    }

    private fun setupOnLoadExpenseByCodeSuccess(expense: Expense){
        binding.apply {
            sv.visibility = View.VISIBLE
            expenseNameTIET.setText(expense.name)
            expenseAmountTIET.setText(String.format("%s",expense.amount.toString()))
            expenseDateTIET.setText(expense.date)
            expenseTimeTIET.setText(expense.time)
            expenseNoteTIET.setText(expense.note)
        }
        initView(expense.code)
    }

    private fun setupOnFailure(message: String){
        val dialogUtil = DialogUtil(this)
        dialogUtil.showErrorDialog(message)
    }

    private fun initView(code: Int){
        setupLoadPickerDialog()
        setupValidation()
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            saveMBt.setOnClickListener { setupEditExpense(code) }
        }
    }

    private fun setupLoadPickerDialog(){
        binding.apply {
            expenseDateTIET.setOnClickListener {
                DatePickerDialogUtil.show(this@EditExpenseActivity) { year,month,day ->
                    expenseDateTIET.setText(getString(R.string.yyyy_mm_dd_format, year,month,day)) } }
            expenseTimeTIET.setOnClickListener {
                TimePickerDialogUtil.show(this@EditExpenseActivity) { hour,minute,amPm ->
                    expenseTimeTIET.setText(getString(R.string.time_format, hour,minute,amPm)) } }
        }
    }

    private fun setupValidation(){
        val textWatcher = TextWatcherUtil.setup {
            val expenseName = binding.expenseNameTIET.text.toString()
            val expenseAmount = binding.expenseAmountTIET.text.toString()
            val expenseDate = binding.expenseDateTIET.text.toString()
            val expenseTime = binding.expenseTimeTIET.text.toString()
            val enable = !TextUtils.isEmpty(expenseName) && !TextUtils.isEmpty(expenseAmount) &&
                         !TextUtils.isEmpty(expenseDate) && !TextUtils.isEmpty(expenseTime)

            binding.apply {
                saveMBt.alpha = if(enable) 1f else 0.5f
                saveMBt.isEnabled = enable
            }
        }
        binding.expenseNameTIET.addTextChangedListener(textWatcher)
        binding.expenseAmountTIET.addTextChangedListener(textWatcher)
        binding.expenseDateTIET.addTextChangedListener(textWatcher)
        binding.expenseTimeTIET.addTextChangedListener(textWatcher)
    }

    private fun setupEditExpense(code: Int){
        val expenseName = binding.expenseNameTIET.text.toString()
        val expenseAmount = binding.expenseAmountTIET.text.toString()
        val expenseDate = binding.expenseDateTIET.text.toString()
        val expenseTime = binding.expenseTimeTIET.text.toString()
        val expenseNote = binding.expenseNoteTIET.text.toString()
        expenseViewModel.edit(code, ExpenseDto(expenseName,expenseAmount.toBigDecimal(),expenseDate,
                                                                           expenseTime,expenseNote))
        setupObserveEditExpense()
    }

    private fun setupObserveEditExpense(){
        val loadingDialogUtil = LoadingDialogUtil(this)
        ObserveResource.setup(
            this,
            expenseViewModel.edited,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> setupOnEditExpenseSuccess(message) },
            onFailure = {}
        )
    }

    private fun setupOnEditExpenseSuccess(message: String){
        ToastUtil.show(this,message)
        setResult(RESULT_OK)
        finish()
    }
}