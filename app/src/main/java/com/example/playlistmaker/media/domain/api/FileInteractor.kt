package com.example.playlistmaker.media.domain.api

interface FileInteractor {
    suspend fun saveFile(pathFile: String): String?
}