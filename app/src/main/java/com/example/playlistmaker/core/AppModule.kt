package com.example.playlistmaker.core

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"

val appModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)
    }
}