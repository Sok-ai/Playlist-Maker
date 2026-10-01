package com.example.playlistmaker.media.domain

import com.example.playlistmaker.media.domain.api.FileInteractor
import com.example.playlistmaker.media.domain.api.FileRepository

class FileInteractorImpl(private val fileRepository: FileRepository) : FileInteractor {
    override suspend fun saveFile(pathFile: String): String? {
        return fileRepository.saveFile(pathFile)
    }
}