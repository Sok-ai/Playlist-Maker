package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val THEME_KEY = "key_for_switcher_theme"

class App : Application() {
    val darkTheme: Boolean
        get() = sharedPref.getBoolean(THEME_KEY, false)
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPref = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        applyTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnable: Boolean) {
        sharedPref
            .edit()
            .putBoolean(THEME_KEY, darkThemeEnable)
            .apply()
        applyTheme(darkThemeEnable)
    }

    private fun applyTheme(darkThemeEnable: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnable) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}