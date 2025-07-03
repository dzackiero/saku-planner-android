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
import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import java.io.FileInputStream
import java.io.OutputStream
import android.widget.Toast
import androidx.core.graphics.scale

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
    var quality: Int
    var stream: ByteArrayOutputStream
    var byteArray: ByteArray

    fun compressLoop(inputBitmap: Bitmap): ByteArray {
        quality = 90
        do {
            stream = ByteArrayOutputStream()
            inputBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            byteArray = stream.toByteArray()
            quality -= 5
        } while (byteArray.size > 1000 * 1024 && quality > 80)
        return byteArray
    }

    byteArray = compressLoop(bitmap)

    if (byteArray.size > 1000 * 1024) {
        val resizedBitmap = bitmap.scale((bitmap.width * 0.80).toInt(), (bitmap.height * 0.80).toInt())
        byteArray = compressLoop(resizedBitmap)
    }
    compressedFile.outputStream().use { it.write(byteArray) }

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

fun saveImageToGallery(context: Context, imageFile: File, appName: String = "YourApp") {
    if (!imageFile.exists()) {
        Timber.e("Source file does not exist: ${imageFile.absolutePath}")
        Toast.makeText(context, "File to save does not exist.", Toast.LENGTH_SHORT).show()
        return
    }

    val resolver = context.contentResolver
    val displayName = "${System.currentTimeMillis()}_${imageFile.name}"
    val mimeType = if (imageFile.name.endsWith(".png", ignoreCase = true)) "image/png" else "image/jpeg"

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/$appName")
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
    }

    val collectionUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    var imageUriInGallery: Uri? = null
    try {
        imageUriInGallery = resolver.insert(collectionUri, contentValues)
    } catch (e: Exception) {
        Timber.e(e, "Failed to insert image into MediaStore.")
        Toast.makeText(context, "Error creating gallery entry.", Toast.LENGTH_SHORT).show()
        return
    }


    if (imageUriInGallery != null) {
        try {
            resolver.openOutputStream(imageUriInGallery).use { outputStream: OutputStream? ->
                if (outputStream == null) {
                    throw Exception("Content resolver returned null OutputStream")
                }
                FileInputStream(imageFile).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(imageUriInGallery, contentValues, null, null)
            }
            Timber.d("Image saved to gallery: $imageUriInGallery")
            Toast.makeText(context, "Foto disimpan ke galeri", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Timber.e(e, "Failed to save image to gallery")
            Toast.makeText(context, "Gagal menyimpan foto ke galeri.", Toast.LENGTH_SHORT).show()
            resolver.delete(imageUriInGallery, null, null)
        }
    } else {
        Timber.e("Failed to create MediaStore entry for image.")
        Toast.makeText(context, "Gagal membuat entry di galeri.", Toast.LENGTH_SHORT).show()
    }
}