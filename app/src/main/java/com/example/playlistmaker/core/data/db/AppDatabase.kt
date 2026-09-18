package com.example.playlistmaker.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.core.data.db.dao.FavoriteSongDao
import com.example.playlistmaker.core.data.db.entity.SongEntity

@Database(
    entities = [SongEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun favoriteSongDao(): FavoriteSongDao
}