package com.apptech.restaurantminipos.util

import android.app.Dialog
import android.content.Context
import com.apptech.restaurantminipos.R

class LoadingDialogUtil(val context: Context) {
    private val dialog: Dialog = Dialog(context)

    init {
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loading_dialog)
    }

    fun show(show: Boolean){
        if(show){
            dialog.show()
        }
        else{
            dialog.dismiss()
        }
    }
}