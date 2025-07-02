package com.apptech.restaurantminipos.util

import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

object ImagePickerUtil {

    fun setup(context: Context,onImagePicked: (Uri) -> Unit): ActivityResultLauncher<String> {
        return (context as AppCompatActivity).registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let{
                onImagePicked(uri)
            }
        }
    }
}