package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.repository.SongsRepositoryImpl
import com.example.playlistmaker.data.history.SearchHistory
import com.example.playlistmaker.data.mapper.MapperNetwork
import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.data.network.api.SongApi
import com.example.playlistmaker.data.player.MediaPlayerImpl
import com.example.playlistmaker.settings.data.ThemeRepositoryImpl
import com.example.playlistmaker.domain.api.MusicPlayer
import com.example.playlistmaker.domain.api.SongsInteractor
import com.example.playlistmaker.domain.api.SongsRepository
import com.example.playlistmaker.settings.domain.api.ThemeRepository
import com.example.playlistmaker.domain.impl.SongsInteractorImpl
import com.example.playlistmaker.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.Navigator
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    private fun getNavigator(): Navigator {
        return ExternalNavigator(appContext)
    }

    fun provideThemeRepository(): ThemeRepository {
        return ThemeRepositoryImpl(getSharedPreferences())
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getNavigator())
    }

    fun provideThemeInteractor(): ThemeInteractor {
        return ThemeInteractorImpl(provideThemeRepository())
    }

    private fun getSearchHistory(): SearchHistory {
        return SearchHistory(getSharedPreferences())
    }

    private fun provideRetrofitService(): SongApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SongApi::class.java)
    }

    private fun provideNetworkClient(): NetworkClient {
        return RetrofitClient(provideRetrofitService())
    }

    private fun getSongsRepository(): SongsRepository {
        return SongsRepositoryImpl(
            provideNetworkClient(),
            getSearchHistory(),
            MapperNetwork()
        )
    }

    fun provideSongsInteractor(): SongsInteractor {
        return SongsInteractorImpl(getSongsRepository())
    }

    private fun provideMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

    fun provideMusicPlayer(): MusicPlayer {
        return MediaPlayerImpl(provideMediaPlayer())
    }

}