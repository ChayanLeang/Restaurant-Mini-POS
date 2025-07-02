package com.apptech.restaurantminipos.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.apptech.restaurantminipos.R

class DialogUtil(context: Context) {

    val dialog: AlertDialog.Builder = AlertDialog.Builder(context)
        .setCancelable(false)
        .setPositiveButton("Close") { dialog, _ ->
            dialog.dismiss()
        }

    fun showWarningDialog(message: String) {
        dialog
            .setTitle("Warning")
            .setMessage(message)
            .setIcon(R.drawable.ic_warning_24_gold)
            .show()
    }

    fun showErrorDialog(message: String) {
        dialog
            .setTitle("Error")
            .setMessage(message)
            .setIcon(R.drawable.ic_error_24_red)
            .show()
    }
}