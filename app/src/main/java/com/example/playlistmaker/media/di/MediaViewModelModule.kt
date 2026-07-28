package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.ui.view_model.FavoriteViewModel
import com.example.playlistmaker.media.ui.view_model.PlaylistViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mediaViewModelModule = module {
    viewModelOf(::FavoriteViewModel)
    viewModelOf(::PlaylistViewModel)
}