package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.domain.FileInteractorImpl
import com.example.playlistmaker.media.domain.PlaylistInteractorImpl
import com.example.playlistmaker.media.domain.api.FileInteractor
import com.example.playlistmaker.media.domain.api.PlaylistInteractor
import org.koin.dsl.module

val mediaInteractorModule = module {
    factory<FileInteractor> {
        FileInteractorImpl(fileRepository = get())
    }
    factory<PlaylistInteractor> {
        PlaylistInteractorImpl()
    }
}