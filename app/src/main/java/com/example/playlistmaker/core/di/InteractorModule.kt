package com.example.playlistmaker.core.di

import com.example.playlistmaker.core.domain.interactor.FavoriteInteractor
import com.example.playlistmaker.core.domain.interactor.FavoriteInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<FavoriteInteractor> {
        FavoriteInteractorImpl(favoriteRepository = get())
    }
}