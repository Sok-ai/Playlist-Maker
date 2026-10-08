package com.example.playlistmaker.core.data

import androidx.room.withTransaction
import com.example.playlistmaker.core.data.converter.PlaylistConverter
import com.example.playlistmaker.core.data.converter.SongInPlaylistConverter
import com.example.playlistmaker.core.data.db.AppDatabase
import com.example.playlistmaker.core.data.db.dao.PlaylistDao
import com.example.playlistmaker.core.data.db.dao.PlaylistTrackRefDao
import com.example.playlistmaker.core.data.db.dao.SongInPlaylistDao
import com.example.playlistmaker.core.data.db.entity.PlaylistEntity
import com.example.playlistmaker.core.data.db.entity.PlaylistTrackRefEntity
import com.example.playlistmaker.core.domain.model.Playlist
import com.example.playlistmaker.core.domain.model.Song
import com.example.playlistmaker.core.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val database: AppDatabase,
    private val playlistDao: PlaylistDao,
    private val songInPlaylistDao: SongInPlaylistDao,
    private val playlistTrackRefDao: PlaylistTrackRefDao,
    private val playlistConverter: PlaylistConverter,
    private val songInPlaylistConverter: SongInPlaylistConverter
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

    override suspend fun updatePlaylist(playlist: Playlist) {
        val playlistEntity = playlistConverter.toPlaylistEntity(playlist)
        playlistDao.updatePlaylist(playlistEntity)
    }

    override suspend fun addTrackToPlaylist(
        playlist: Playlist,
        song: Song
    ) {
        val ref = PlaylistTrackRefEntity(
            playlistId = playlist.id,
            songId = song.trackId
        )
        val songInPlaylistEntity = songInPlaylistConverter.toEntity(song)
        database.withTransaction {
            songInPlaylistDao.insertSong(songInPlaylistEntity)
            playlistTrackRefDao.insertRef(ref)
            playlistDao.incrementSongCount(playlist.id)
        }
    }

    override suspend fun isSongInPlaylist(playlistId: Long, songId: Long): Boolean =
        playlistTrackRefDao.isSongInPlaylist(playlistId, songId)


    private fun convertEntitiesToPlaylist(playlistList: List<PlaylistEntity>): List<Playlist> =
        playlistList.map {
            playlistConverter.toPlaylist(it)
        }
}