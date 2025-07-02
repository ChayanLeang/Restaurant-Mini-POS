package com.apptech.restaurantminipos.util

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

object ResultLauncherUtil {

    fun setup(activity: ComponentActivity,block: () -> Unit): ActivityResultLauncher<Intent>{
        return activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            if(result.resultCode == RESULT_OK){
                block()
            }
        }
    }
}