package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.StorageClient
import com.example.playlistmaker.search.data.api.SongApi
import com.example.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.impl.SearchNetworkRepositoryImpl
import com.example.playlistmaker.search.data.mapper.MapperNetwork
import com.example.playlistmaker.search.data.network.RetrofitClient
import com.example.playlistmaker.search.data.storage.PrefStorageClient
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.SearchNetworkRepository
import com.example.playlistmaker.search.domain.model.Song
import com.google.gson.reflect.TypeToken
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val SONG_SEARCH_HISTORY_KEY = "song_history_key"
private const val BASE_URL = "https://itunes.apple.com/"
val searchDataModule = module {
    single<SongApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SongApi::class.java)
    }
    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(
            storage = get(),
            sharedPref = get()
        )
    }
    factory<SearchNetworkRepository> {
        SearchNetworkRepositoryImpl(
            networkClient = get(),
            mapper = get()
        )
    }
    single<MapperNetwork> { MapperNetwork() }
    factory<NetworkClient> {
        RetrofitClient(
            context = get(),
            networkService = get()
        )
    }

    single<StorageClient<List<Song>>> {
        PrefStorageClient(
            sharedPref = get(),
            dataName = SONG_SEARCH_HISTORY_KEY,
            type = object : TypeToken<List<Song>>() {}.type
        )
    }
}