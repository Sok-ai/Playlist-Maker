package com.example.playlistmaker.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.core.data.db.entity.SongInPlaylistEntity

@Dao
interface SongInPlaylistDao {
    @Insert(SongInPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(song: SongInPlaylistEntity)
}