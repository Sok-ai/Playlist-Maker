package com.example.playlistmaker.creator

import com.example.playlistmaker.core.appModule
import com.example.playlistmaker.library.di.libraryModule
import com.example.playlistmaker.library.di.libraryViewModelModule
import com.example.playlistmaker.media.di.mediaViewModelModule
import com.example.playlistmaker.search.di.searchDataModule
import com.example.playlistmaker.search.di.searchDomainModule
import com.example.playlistmaker.search.di.searchViewModelModule
import com.example.playlistmaker.settings.di.settingDataModule
import com.example.playlistmaker.settings.di.settingDomainModule
import com.example.playlistmaker.settings.di.settingViewModelModule
import com.example.playlistmaker.sharing.di.sharingDataModule
import com.example.playlistmaker.sharing.di.sharingDomainModule
import org.koin.core.module.Module

object Creator {
    fun getModules(): List<Module> {
        return listOf(
            appModule,
            libraryModule,
            libraryViewModelModule,
            searchDomainModule,
            searchDataModule,
            searchViewModelModule,
            settingDataModule,
            settingDomainModule,
            settingViewModelModule,
            sharingDomainModule,
            sharingDataModule,
            mediaViewModelModule
        )
    }
}