package com.example.playlistmaker.media.data.file

import androidx.core.net.toUri
import com.example.playlistmaker.media.domain.api.FileRepository

class FileRepositoryImpl(private val fileClient: FileClient) : FileRepository {
    override suspend fun saveFile(pathFile: String): String? {
        return fileClient.saveCover(pathFile.toUri())
    }
}