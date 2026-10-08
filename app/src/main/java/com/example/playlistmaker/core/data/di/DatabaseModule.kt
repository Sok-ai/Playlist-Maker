package com.example.playlistmaker.core.data.di

import androidx.room.Room
import com.example.playlistmaker.core.data.db.AppDatabase
import com.example.playlistmaker.core.data.db.dao.FavoriteSongDao
import com.example.playlistmaker.core.data.db.dao.PlaylistDao
import com.example.playlistmaker.core.data.db.dao.PlaylistTrackRefDao
import com.example.playlistmaker.core.data.db.dao.SongInPlaylistDao
import com.example.playlistmaker.core.data.db.migration.MIGRATION_1_2
import com.example.playlistmaker.core.data.db.migration.MIGRATION_2_3
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        Room
            .databaseBuilder(androidContext(), AppDatabase::class.java, "database")
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
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
    single<PlaylistTrackRefDao> {
        get<AppDatabase>().playlistTrackRefDao()
    }
}