package com.example.playlistmaker.core.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.core.data.db.entity.SongInPlaylistEntity

@Dao
interface SongInPlaylistDao {
    @Insert(entity = SongInPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(song: SongInPlaylistEntity)

    @Query("DELETE FROM track_in_playlist_table WHERE song_id = :songId")
    suspend fun deleteSong(songId: Long)
}