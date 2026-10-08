package com.example.playlistmaker.core.di

import com.example.playlistmaker.core.domain.interactor.FavoriteInteractor
import com.example.playlistmaker.core.domain.interactor.FavoriteInteractorImpl
import com.example.playlistmaker.core.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.core.domain.interactor.PlaylistInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<FavoriteInteractor> {
        FavoriteInteractorImpl(favoriteRepository = get())
    }
    single<PlaylistInteractor> {
        PlaylistInteractorImpl(playlistRepository = get())
    }
}