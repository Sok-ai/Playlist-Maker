package com.example.playlistmaker.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.core.data.db.dao.FavoriteSongDao
import com.example.playlistmaker.core.data.db.entity.SongEntity
import com.example.playlistmaker.core.data.db.dao.PlaylistDao
import com.example.playlistmaker.core.data.db.entity.PlaylistEntity

@Database(
    entities = [SongEntity::class, PlaylistEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteSongDao(): FavoriteSongDao
    abstract fun playlistDao(): PlaylistDao
}