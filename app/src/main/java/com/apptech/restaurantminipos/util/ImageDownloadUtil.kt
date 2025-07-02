package com.apptech.restaurantminipos.util

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

object ImageDownloadUtil {

    suspend fun download(context: Context,imageUrl: String): File {
        val rawFileName = imageUrl.substringAfterLast("/")
            .replace("[^a-zA-Z0-9._-]".toRegex(), "_")

        val maxFileNameLength = 255
        val fileName = if (rawFileName.length > maxFileNameLength) {
            rawFileName.take(maxFileNameLength)
        } else {
            rawFileName
        }
        val tempFile = File(context.cacheDir, fileName)

        withContext(Dispatchers.IO) {
            val url = URL(imageUrl)
            url.openStream().use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
        return tempFile
    }
}