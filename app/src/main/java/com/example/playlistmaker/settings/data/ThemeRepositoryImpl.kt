package com.example.playlistmaker.settings.data

import android.content.SharedPreferences
import com.example.playlistmaker.settings.domain.api.ThemeRepository

const val THEME_KEY = "key_for_switcher_theme"
class ThemeRepositoryImpl(private val sharedPref: SharedPreferences) : ThemeRepository {
    override fun getTheme(): Boolean = sharedPref.getBoolean(THEME_KEY, false)
    override fun putTheme(isDark: Boolean) {
        sharedPref.edit().putBoolean(THEME_KEY, isDark).apply()
    }
}