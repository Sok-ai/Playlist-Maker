package com.example.playlistmaker.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.core.data.db.entity.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteSongDao {
    @Query("SELECT * FROM favorite_songs_table ORDER BY add_time DESC")
    fun getFavorites(): Flow<List<SongEntity>>

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_songs_table WHERE song_id = :idSong)")
    fun isFavoriteById(idSong: Long): Flow<Boolean>

    @Query("DELETE FROM favorite_songs_table WHERE song_id = :id")
    suspend fun deleteFavoriteById(id: Long)

    @Insert(entity = SongEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(songEntity: SongEntity)
}