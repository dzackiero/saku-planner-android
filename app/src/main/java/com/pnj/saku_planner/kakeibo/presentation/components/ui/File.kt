package com.pnj.saku_planner.kakeibo.presentation.components.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(TIMESTAMP_FORMAT, Locale.US).format(Date())

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

fun deleteTempFile(file: File): Boolean {
    return try {
        if (file.exists()) {
            file.delete()
        } else {
            false
        }
    } catch (e: Exception) {
        false
    }
}

fun compressImageFile(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    val compressedFile = File(file.parent, "compressed_${file.name}")

    var quality = 100
    var stream: ByteArrayOutputStream
    var byteArray: ByteArray

    do {
        stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        byteArray = stream.toByteArray()
        quality -= 5
    } while (byteArray.size > 600 * 1024 && quality > 10)

    val out = FileOutputStream(compressedFile)
    out.write(byteArray)
    out.flush()
    out.close()

    return compressedFile
}



fun uriToFile(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val tempFile = File.createTempFile("image", ".jpg", context.cacheDir)
        inputStream.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        tempFile
    } catch (e: Exception) {
        Timber.e("Failed to convert uri to file: ${e.message}")
        null
    }
}
