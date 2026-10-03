package com.example.playlistmaker.media.domain.api

import com.example.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun insertPlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
}