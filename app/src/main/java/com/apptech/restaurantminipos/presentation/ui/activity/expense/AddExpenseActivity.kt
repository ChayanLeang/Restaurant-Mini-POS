package com.apptech.restaurantminipos.presentation.ui.activity.expense

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.apptech.restaurantminipos.R
import com.apptech.restaurantminipos.data.dto.ExpenseDto
import com.apptech.restaurantminipos.databinding.ActivityAddExpenseBinding
import com.apptech.restaurantminipos.presentation.viewmodel.ExpenseViewModel
import com.apptech.restaurantminipos.util.DatePickerDialogUtil
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.TextWatcherUtil
import com.apptech.restaurantminipos.util.TimePickerDialogUtil
import com.apptech.restaurantminipos.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddExpenseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddExpenseBinding
    private val expenseViewModel : ExpenseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        setupLoadPickerDialog()
        setupValidation()
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            saveMBt.setOnClickListener { setupAddExpense() }
        }
    }

    private fun setupLoadPickerDialog(){
        binding.apply {
            expenseDateTIET.setOnClickListener { DatePickerDialogUtil.show(this@AddExpenseActivity) {
                            year,month,day -> expenseDateTIET.setText(getString(R.string.yyyy_mm_dd_format,
                                                                                       year,month,day)) } }
            expenseTimeTIET.setOnClickListener { TimePickerDialogUtil.show(this@AddExpenseActivity) {
                            hour,minute,amPm -> expenseTimeTIET.setText(getString(R.string.time_format,
                                                                                 hour,minute,amPm)) } }
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

    private fun setupAddExpense(){
        val expenseName = binding.expenseNameTIET.text.toString()
        val expenseAmount = binding.expenseAmountTIET.text.toString()
        val expenseDate = binding.expenseDateTIET.text.toString()
        val expenseTime = binding.expenseTimeTIET.text.toString()
        val expenseNote = binding.expenseNoteTIET.text.toString()
        expenseViewModel.add(ExpenseDto(expenseName,expenseAmount.toBigDecimal(), expenseDate,
                                                                     expenseTime,expenseNote))
        setupObserveAddExpense()
    }

    private fun setupObserveAddExpense(){
        val loadingDialogUtil = LoadingDialogUtil(this)
        ObserveResource.setup(
            this,
            expenseViewModel.added,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> setupOnSuccess(message)},
            onFailure = { }
        )
    }

    private fun setupOnSuccess(message: String){
        ToastUtil.show(this,message)
        setResult(RESULT_OK)
        finish()
    }
}