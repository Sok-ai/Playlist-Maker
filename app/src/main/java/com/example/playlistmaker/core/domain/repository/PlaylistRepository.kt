package com.example.playlistmaker.core.domain.repository

import com.example.playlistmaker.core.domain.model.Playlist
import com.example.playlistmaker.core.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun insertPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlist: Playlist, song: Song)
}