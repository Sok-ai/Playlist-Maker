package com.example.playlistmaker.core.data.di

import androidx.room.Room
import com.example.playlistmaker.core.data.db.AppDatabase
import com.example.playlistmaker.core.data.db.dao.FavoriteSongDao
import com.example.playlistmaker.core.data.db.dao.PlaylistDao
import com.example.playlistmaker.core.data.db.dao.SongInPlaylistDao
import com.example.playlistmaker.core.data.db.migration.MIGRATION_1_2
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        Room
            .databaseBuilder(androidContext(), AppDatabase::class.java, "database")
            .addMigrations(MIGRATION_1_2)
            .build()
    }
    single<FavoriteSongDao> {
        get<AppDatabase>().favoriteSongDao()
    }
    single<PlaylistDao> {
        get<AppDatabase>().playlistDao()
    }
    single<SongInPlaylistDao> {
        get<AppDatabase>().songInPlaylistDao()
    }
}