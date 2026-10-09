package com.example.playlistmaker.core.domain.interactor

import com.example.playlistmaker.core.domain.model.Playlist
import com.example.playlistmaker.core.domain.model.Song
import com.example.playlistmaker.core.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {
    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistRepository.insertPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> =
        playlistRepository.getPlaylists()

    override suspend fun getPlaylistById(id: Long): Playlist? =
        playlistRepository.getPlaylistById(id)

    override suspend fun deleteSong(playlistId: Long, songId: Long) {
        playlistRepository.deleteSong(playlistId, songId)
    }

    override fun getTracksForPlaylist(id: Long): Flow<List<Song>> =
        playlistRepository.getTracksForPlaylist(id)

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(
        playlist: Playlist,
        song: Song
    ) {
        playlistRepository.addTrackToPlaylist(playlist, song)
    }

    override suspend fun isSongInPlaylist(
        playlistId: Long,
        songId: Long
    ): Boolean = playlistRepository.isSongInPlaylist(playlistId, songId)
}