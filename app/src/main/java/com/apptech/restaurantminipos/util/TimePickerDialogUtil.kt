package com.apptech.restaurantminipos.util

import android.app.TimePickerDialog
import android.content.Context
import java.util.Calendar

object TimePickerDialogUtil {

    fun show(context: Context,onTimeSelected:(hour: Int,minute: Int,amPm: String) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(context, { _, selectedHour, selectedMinute ->
            val displayHour = if (selectedHour % 12 == 0) 12 else selectedHour % 12
            val displayAmPm = if (hour >= 12) "PM" else "AM"
            onTimeSelected(displayHour,selectedMinute,displayAmPm)
        }, hour, minute, false)

        timePickerDialog.show()
    }
}