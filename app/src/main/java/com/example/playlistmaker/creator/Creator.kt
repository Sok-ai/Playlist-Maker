package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.impl.SearchNetworkRepositoryImpl
import com.example.playlistmaker.search.data.mapper.MapperNetwork
import com.example.playlistmaker.search.data.network.RetrofitClient
import com.example.playlistmaker.search.data.api.SongApi
import com.example.playlistmaker.library.data.impl.MediaPlayerImpl
import com.example.playlistmaker.settings.data.ThemeRepositoryImpl
import com.example.playlistmaker.library.domain.api.MusicPlayer
import com.example.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.storage.PrefStorageClient
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.SearchRepository
import com.example.playlistmaker.settings.domain.api.ThemeRepository
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.search.domain.model.Song
import com.example.playlistmaker.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.Navigator
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val SONG_SEARCH_HISTORY_KEY = "song_history_key"

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

    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
            PrefStorageClient(
                appContext,
                SONG_SEARCH_HISTORY_KEY,
                object : TypeToken<List<Song>>() {}.type
            ),
            getSharedPreferences()
        )
    }

    private fun provideRetrofitService(): SongApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SongApi::class.java)
    }

    private fun provideNetworkClient(): NetworkClient {
        return RetrofitClient(appContext, provideRetrofitService())
    }

    private fun getSearchNetworkRepository(): SearchRepository {
        return SearchNetworkRepositoryImpl(
            provideNetworkClient(),
            MapperNetwork()
        )
    }

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(getSearchNetworkRepository(), getSearchHistoryRepository())
    }

    private fun provideMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

    fun provideMusicPlayer(): MusicPlayer {
        return MediaPlayerImpl(provideMediaPlayer())
    }

}