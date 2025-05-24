package com.pnj.saku_planner.kakeibo.presentation.components

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(TIMESTAMP_FORMAT, Locale.US).format(Date())

fun CreateCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

fun DeleteTempFile(file: File): Boolean {
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