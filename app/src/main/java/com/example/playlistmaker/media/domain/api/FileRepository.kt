package com.example.playlistmaker.media.domain.api

interface FileRepository {
    suspend fun saveFile(pathFile: String): String?
}