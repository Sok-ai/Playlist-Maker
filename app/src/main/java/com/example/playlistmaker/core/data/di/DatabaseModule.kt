package com.example.playlistmaker.core.data.di

import androidx.room.Room
import com.example.playlistmaker.core.data.db.AppDatabase
import com.example.playlistmaker.core.data.db.dao.FavoriteSongDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        Room
            .databaseBuilder(androidContext(), AppDatabase::class.java, "database")
            .build()
    }
    single<FavoriteSongDao> {
        get<AppDatabase>().favoriteSongDao()
    }
}