package com.example.playlistmaker.core.domain.repository

import com.example.playlistmaker.core.domain.model.Playlist
import com.example.playlistmaker.core.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun insertPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun getPlaylistById(id: Long): Playlist?
    fun getPlaylistByIdFlow(id: Long): Flow<Playlist?>

    fun getTracksForPlaylist(id: Long): Flow<List<Song>>
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlist: Playlist, song: Song)

    suspend fun deletePlaylist(playlistId: Long)

    suspend fun deleteSong(playlistId: Long, songId: Long)
    suspend fun isSongInPlaylist(playlistId: Long, songId: Long): Boolean
}