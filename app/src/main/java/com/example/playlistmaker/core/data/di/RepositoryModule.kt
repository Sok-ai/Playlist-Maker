package com.example.playlistmaker.core.data.di

import com.example.playlistmaker.core.data.FavoriteRepositoryImpl
import com.example.playlistmaker.core.data.converter.FavoriteConverter
import com.example.playlistmaker.core.domain.repository.FavoriteRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<FavoriteRepository> {
        FavoriteRepositoryImpl(favoriteSongDao = get(), favoriteConverter = FavoriteConverter())
    }
}