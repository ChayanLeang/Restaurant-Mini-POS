package com.apptech.restaurantminipos.util

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.apptech.restaurantminipos.R

object ToastUtil {

    fun show(context: Context,message: String) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.custom_toast, null)
        val toastText : TextView = layout.findViewById(R.id.tv)
        toastText.text = message
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
}