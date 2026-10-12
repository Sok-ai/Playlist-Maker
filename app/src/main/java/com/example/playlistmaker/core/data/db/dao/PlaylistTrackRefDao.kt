package com.example.playlistmaker.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.core.data.db.entity.PlaylistTrackRefEntity
import com.example.playlistmaker.core.data.db.entity.SongInPlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistTrackRefDao {
    @Insert(entity = PlaylistTrackRefEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRef(ref: PlaylistTrackRefEntity)

    @Query(
        """SELECT EXISTS (
        SELECT 1 FROM playlist_track_ref
        WHERE ref_playlist_id = :playlistId AND ref_song_id = :songId)"""
    )
    suspend fun isSongInPlaylist(playlistId: Long, songId: Long): Boolean

    @Query(
        """
    SELECT t.* FROM track_in_playlist_table AS t
    INNER JOIN playlist_track_ref AS r
    ON r.ref_song_id = t.song_id
    WHERE r.ref_playlist_id = :playlistId
    ORDER BY r.added_at DESC
"""
    )
    fun getSongFromPlaylist(playlistId: Long): Flow<List<SongInPlaylistEntity>>

    @Query(
        """
        DELETE FROM playlist_track_ref WHERE ref_playlist_id = :playlistId AND ref_song_id = :songId
    """
    )
    suspend fun deleteRef(playlistId: Long, songId: Long)

    @Query("SELECT COUNT(*) FROM playlist_track_ref WHERE ref_song_id = :songId")
    suspend fun countPlaylistsWithTrack(songId: Long): Int

    @Query("SELECT ref_song_id FROM playlist_track_ref WHERE ref_playlist_id = :playlistId")
    suspend fun getSongIdsFromPlaylist(playlistId: Long): List<Long>
}
