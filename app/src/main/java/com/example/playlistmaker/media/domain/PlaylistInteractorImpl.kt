package com.example.playlistmaker.media.domain

import com.example.playlistmaker.media.domain.api.PlaylistInteractor
import com.example.playlistmaker.media.domain.api.PlaylistRepository
import com.example.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {
    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistRepository.insertPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> =
        playlistRepository.getPlaylists()
}