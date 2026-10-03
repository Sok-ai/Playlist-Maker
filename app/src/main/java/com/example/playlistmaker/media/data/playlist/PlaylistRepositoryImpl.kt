package com.example.playlistmaker.media.data.playlist

import com.example.playlistmaker.media.data.db.converter.PlaylistConverter
import com.example.playlistmaker.media.data.db.dao.PlaylistDao
import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.domain.api.PlaylistRepository
import com.example.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistConverter: PlaylistConverter
) : PlaylistRepository {
    override suspend fun insertPlaylist(playlist: Playlist) {
        val playlistEntity = playlistConverter.toPlaylistEntity(playlist)
        playlistDao.insertPlaylist(playlistEntity)
    }

    override fun getPlaylists(): Flow<List<Playlist>> =
        playlistDao.getPlaylists()
            .map {
                convertEntitiesToPlaylist(it)
            }
            .distinctUntilChanged()

    private fun convertEntitiesToPlaylist(playlistList: List<PlaylistEntity>): List<Playlist> =
        playlistList.map {
            playlistConverter.toPlaylist(it)
        }
}