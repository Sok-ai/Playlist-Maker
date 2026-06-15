package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.data.repository.SongsRepositoryImpl
import com.example.playlistmaker.data.history.SearchHistory
import com.example.playlistmaker.data.mapper.MapperNetwork
import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.data.player.MediaPlayerImpl
import com.example.playlistmaker.domain.api.MusicPlayer
import com.example.playlistmaker.domain.api.SongsInteractor
import com.example.playlistmaker.domain.api.SongsRepository
import com.example.playlistmaker.domain.impl.SongsInteractorImpl

object Creator {
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private fun getSharedPreferences(): SharedPreferences {
        return appContext.getSharedPreferences(
            PLAYLIST_MAKER_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    private fun getSearchHistory(): SearchHistory {
        return SearchHistory(getSharedPreferences())
    }

    private fun getSongsRepository(): SongsRepository {
        return SongsRepositoryImpl(
            RetrofitClient(),
            getSearchHistory(),
            MapperNetwork()
        )
    }

    fun provideSongsInteractor(): SongsInteractor {
        return SongsInteractorImpl(getSongsRepository())
    }

    fun provideMusicPlayer(): MusicPlayer {
        return MediaPlayerImpl()
    }
}