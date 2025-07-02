package com.apptech.restaurantminipos.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher

object NavigationUtil {
    inline fun <reified T : Activity> navigateTo(context: Context,extras: Bundle ?= null,
                                                 resultLauncher: ActivityResultLauncher<Intent> ?= null) {
        val intent = Intent(context, T::class.java)
        extras?.let {
            intent.putExtras(it)
        }
        resultLauncher?.launch(intent) ?: context.startActivity(intent)
    }
}