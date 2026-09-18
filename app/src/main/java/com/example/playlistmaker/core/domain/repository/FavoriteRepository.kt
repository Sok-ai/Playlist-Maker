package com.example.playlistmaker.core.domain.repository

import com.example.playlistmaker.core.data.db.entity.SongEntity
import com.example.playlistmaker.core.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun getFavorites(): Flow<List<Song>>
    suspend fun deleteFavorites(idSong: Long)
    suspend fun insertFavorite(song: Song)
    suspend fun isFavorite(idSong: Long): Flow<Boolean>
}