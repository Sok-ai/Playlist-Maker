package com.example.playlistmaker.core.data.di

import com.example.playlistmaker.core.data.FavoriteRepositoryImpl
import com.example.playlistmaker.core.data.PlaylistRepositoryImpl
import com.example.playlistmaker.core.data.converter.FavoriteConverter
import com.example.playlistmaker.core.data.converter.PlaylistConverter
import com.example.playlistmaker.core.domain.repository.FavoriteRepository
import com.example.playlistmaker.core.domain.repository.PlaylistRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<FavoriteRepository> {
        FavoriteRepositoryImpl(favoriteSongDao = get(), favoriteConverter = FavoriteConverter())
    }
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(playlistDao = get(), PlaylistConverter())
    }
}