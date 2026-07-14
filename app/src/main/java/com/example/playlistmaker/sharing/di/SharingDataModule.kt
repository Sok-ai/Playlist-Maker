package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.Navigator
import org.koin.dsl.module

val sharingDataModule = module {
    single<Navigator> {
        ExternalNavigator(context = get())
    }
}