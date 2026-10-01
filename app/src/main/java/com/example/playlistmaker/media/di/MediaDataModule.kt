package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.data.FileRepositoryImpl
import com.example.playlistmaker.media.data.PlaylistRepositoryImpl
import com.example.playlistmaker.media.data.file.FileClient
import com.example.playlistmaker.media.domain.api.FileRepository
import com.example.playlistmaker.media.domain.api.PlaylistRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mediaDataModule = module {
    single<FileRepository> {
        FileRepositoryImpl(fileClient = get())
    }
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(playlistDao = get())
    }
    single<FileClient> {
        FileClient(androidContext())
    }
}