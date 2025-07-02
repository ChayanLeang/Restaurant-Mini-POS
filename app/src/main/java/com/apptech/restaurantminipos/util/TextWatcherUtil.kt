package com.apptech.restaurantminipos.util

import android.text.Editable
import android.text.TextWatcher

object TextWatcherUtil {

    fun setup(block: () -> Unit) = object: TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            block()
        }

        override fun afterTextChanged(s: Editable?) {}
    }
}