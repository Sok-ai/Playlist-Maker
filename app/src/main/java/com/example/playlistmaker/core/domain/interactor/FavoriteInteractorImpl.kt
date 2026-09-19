package com.example.playlistmaker.core.domain.interactor

import com.example.playlistmaker.core.domain.model.Song
import com.example.playlistmaker.core.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteRepository) :
    FavoriteInteractor {
    override suspend fun getFavoriteById(idSong: Long): Song? =
        favoriteRepository.getFavoriteById(idSong)

    override fun getFavorites(): Flow<List<Song>> =
        favoriteRepository.getFavorites()

    override suspend fun deleteFavorites(idSong: Long) {
        favoriteRepository.deleteFavorites(idSong)
    }

    override suspend fun insertFavorite(song: Song) {
        favoriteRepository.insertFavorite(song)
    }

    override fun isFavorite(idSong: Long): Flow<Boolean> =
        favoriteRepository.isFavorite(idSong = idSong)
}