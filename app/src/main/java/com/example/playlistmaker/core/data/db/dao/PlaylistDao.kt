package com.example.playlistmaker.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.core.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * from playlist_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>
}