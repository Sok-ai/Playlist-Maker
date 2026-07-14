package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.ui.view_model.LibraryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val libraryViewModelModule = module {
    viewModel { params ->
        LibraryViewModel(
            musicPlayer = get(),
            searchInteractor = get(),
            songId = params.get<Long>()
        )
    }
}