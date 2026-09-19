package com.example.playlistmaker.core.data

import com.example.playlistmaker.core.data.converter.FavoriteConverter
import com.example.playlistmaker.core.data.db.dao.FavoriteSongDao
import com.example.playlistmaker.core.data.db.entity.SongEntity
import com.example.playlistmaker.core.domain.model.Song
import com.example.playlistmaker.core.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
    private val favoriteSongDao: FavoriteSongDao,
    private val favoriteConverter: FavoriteConverter
) : FavoriteRepository {

    override fun getFavorites(): Flow<List<Song>> =
        favoriteSongDao.getFavorites().map {
            songEntitiesToSong(it)
        }.distinctUntilChanged()

    override fun isFavorite(idSong: Long): Flow<Boolean> =
        favoriteSongDao.isFavoriteById(idSong).distinctUntilChanged()

    override suspend fun getFavoriteById(idSong: Long): Song? {
        val songEntity = favoriteSongDao.getFavoriteById(idSong)
        return songEntity?.let { songEntityToSong(it) }
    }


    override suspend fun deleteFavorites(idSong: Long) {
        favoriteSongDao.deleteFavoriteById(idSong)
    }

    override suspend fun insertFavorite(song: Song) {
        val songEntity = songToSongEntity(song)
        favoriteSongDao.insertFavorite(songEntity)
    }

    private fun songEntityToSong(songEntity: SongEntity): Song {
        return favoriteConverter.toSong(songEntity)
    }

    private fun songEntitiesToSong(listSongEntity: List<SongEntity>): List<Song> =
        listSongEntity.map { songEntity -> songEntityToSong(songEntity) }

    private fun songToSongEntity(song: Song): SongEntity =
        favoriteConverter.toSongEntity(song)

}