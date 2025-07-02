package com.apptech.restaurantminipos.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object MultiPartUtil {

    fun createImagePartFromUri(context: Context,uri: Uri): MultipartBody.Part? {
        val file = context.getFileFromUri(uri) ?: return null
        val requestBody = file.asRequestBody("image/*".toMediaType())
        return MultipartBody.Part.createFormData("image", file.name, requestBody)
    }

    fun createImagePartFromImageDownload(file: File?) : MultipartBody.Part {
        val requestBody = file?.asRequestBody("image/*".toMediaType())
        return MultipartBody.Part.createFormData("image", file?.name, requestBody!!)
    }

    fun createStringPart(content : String) : RequestBody{
        return content.toRequestBody("text/plain".toMediaType())
    }

    private fun Context.getFileFromUri(uri: Uri): File? {
        val fileName = contentResolver.query(uri, null, null, null,
            null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        } ?: return null

        val tempFile = File(cacheDir, fileName)
        tempFile.outputStream().use { outputStream ->
            contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return tempFile
    }
}