package com.example.playlistmaker.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.core.data.db.entity.PlaylistTrackRefEntity

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
}
