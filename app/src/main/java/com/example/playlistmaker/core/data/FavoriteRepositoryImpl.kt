package com.example.playlistmaker.core.data

import com.example.playlistmaker.core.data.converter.FavoriteConverter
import com.example.playlistmaker.core.data.db.AppDatabase
import com.example.playlistmaker.core.data.db.entity.SongEntity
import com.example.playlistmaker.core.domain.model.Song
import com.example.playlistmaker.core.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val favoriteConverter: FavoriteConverter
) : FavoriteRepository {

    override suspend fun getFavorites(): Flow<List<Song>> =
        appDatabase.favoriteSongDao().getFavorites().map {
            songEntitiesToSong(it)
        }

    override suspend fun isFavorite(idSong: Long): Flow<Boolean> =
        appDatabase.favoriteSongDao().isFavoriteById(idSong)


    override suspend fun deleteFavorites(idSong: Long) {
        appDatabase.favoriteSongDao().deleteFavoriteById(idSong)
    }

    override suspend fun insertFavorite(song: Song) {
        val songEntity = songToSongEntity(song)
        appDatabase.favoriteSongDao().insertFavorite(songEntity)
    }

    private fun songEntitiesToSong(listSongEntity: List<SongEntity>): List<Song> =
        listSongEntity.map { songEntity -> favoriteConverter.toSong(songEntity) }

    private fun songToSongEntity(song: Song): SongEntity =
        favoriteConverter.toSongEntity(song)

}