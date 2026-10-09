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
import com.example.playlistmaker.core.data.db.entity.SongInPlaylistEntity
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

    override suspend fun getPlaylistById(id: Long): Playlist? {
        val entity = playlistDao.getPlaylistById(id) ?: return null
        return playlistConverter.toPlaylist(entity)
    }

    override fun getTracksForPlaylist(id: Long): Flow<List<Song>> {
        return playlistTrackRefDao.getSongFromPlaylist(id)
            .distinctUntilChanged()
            .map { convertEntitiesToSong(it) }
    }

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
            songId = song.trackId,
            addedAt = System.currentTimeMillis()
        )
        val songInPlaylistEntity = songInPlaylistConverter.toEntity(song)
        database.withTransaction {
            songInPlaylistDao.insertSong(songInPlaylistEntity)
            playlistTrackRefDao.insertRef(ref)
            playlistDao.incrementSongCount(playlist.id)
        }
    }

    override suspend fun deleteSong(playlistId: Long, songId: Long) {
        database.withTransaction {
            playlistTrackRefDao.deleteRef(playlistId, songId)
            val count = playlistTrackRefDao.countPlaylistsWithTrack(songId)
            if (count == 0) songInPlaylistDao.deleteSong(songId)
            playlistDao.decrementSongCount(playlistId)
        }
    }


    override suspend fun isSongInPlaylist(playlistId: Long, songId: Long): Boolean =
        playlistTrackRefDao.isSongInPlaylist(playlistId, songId)


    private fun convertEntitiesToPlaylist(playlistList: List<PlaylistEntity>): List<Playlist> =
        playlistList.map {
            playlistConverter.toPlaylist(it)
        }

    private fun convertEntitiesToSong(songList: List<SongInPlaylistEntity>): List<Song> =
        songList.map {
            songInPlaylistConverter.toSong(it)
        }
}