package com.example.playlistmaker.media.di

import com.example.playlistmaker.core.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.dao.PlaylistDao
import org.koin.dsl.module

val mediaDatabaseModule = module {
    single<PlaylistDao> {
        get<AppDatabase>().playlistDao()
    }
}