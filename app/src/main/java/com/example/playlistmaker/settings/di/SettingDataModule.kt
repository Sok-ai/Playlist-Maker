package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.data.ThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.api.ThemeRepository
import org.koin.dsl.module

val settingDataModule = module {
    single<ThemeRepository> {
        ThemeRepositoryImpl(sharedPref = get())
    }
}