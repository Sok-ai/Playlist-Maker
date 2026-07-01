package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"

class App : Application() {
    val themeSwitcher = Creator.provideThemeRepository()
    val darkTheme: Boolean
        get() = themeSwitcher.getTheme()

    override fun onCreate() {
        super.onCreate()
        Creator.init(this)
        applyTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnable: Boolean) {
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