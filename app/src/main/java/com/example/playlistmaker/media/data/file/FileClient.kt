package com.example.playlistmaker.media.data.file

import android.content.Context
import android.net.Uri
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class FileClient(private val context: Context) {
    suspend fun saveCover(uri: Uri): String? = withContext(Dispatchers.IO) {
        val baseDir =
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: context.filesDir
        val fileDir =
            File(baseDir, "images")
                .apply { mkdirs() }
        val file = File(fileDir, "image_${UUID.randomUUID()}.jpg")

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        } ?: return@withContext null

        file.absolutePath
    }
}