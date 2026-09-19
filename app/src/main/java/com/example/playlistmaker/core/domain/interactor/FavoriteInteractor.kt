package com.example.playlistmaker.core.domain.interactor

import com.example.playlistmaker.core.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {
    suspend fun getFavoriteById(idSong: Long): Song?
    fun getFavorites(): Flow<List<Song>>
    suspend fun deleteFavorites(idSong: Long)
    suspend fun insertFavorite(song: Song)
    fun isFavorite(idSong: Long): Flow<Boolean>
}