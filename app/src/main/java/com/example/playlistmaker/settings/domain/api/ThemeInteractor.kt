package com.example.playlistmaker.settings.domain.api

interface ThemeInteractor {
    fun getTheme(): Boolean
    fun putTheme(isDark: Boolean)
}