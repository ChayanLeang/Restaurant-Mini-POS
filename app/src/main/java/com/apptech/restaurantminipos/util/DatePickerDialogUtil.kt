package com.apptech.restaurantminipos.util

import android.app.DatePickerDialog
import android.content.Context
import java.util.Calendar

object DatePickerDialogUtil {

    fun show(context: Context,onDateSelected: (year: Int, month: Int, dayOfMonth: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                onDateSelected(selectedYear, selectedMonth, selectedDayOfMonth)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}